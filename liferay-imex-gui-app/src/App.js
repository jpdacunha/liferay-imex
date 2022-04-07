import React, { useState, useEffect } from 'react'
import ClayAlert from '@clayui/alert'
import ClayLayout from '@clayui/layout'
import ClayIcon from '@clayui/icon'
import ClayButton from '@clayui/button'
import TaskList from '@components/TaskList/TaskList'
import '@clayui/css/lib/css/atlas.css'
import './App.css'
import spritemap from '@images/icons.svg'
import { trackPromise } from 'react-promise-tracker'
import client from '@commons/axios-client'
import { useTranslation } from 'react-i18next'
import LoadingIndicator from '@components/LoadingIndicator/LoadingIndicator'
import { useErrorHandler } from 'react-error-boundary'
import AppContainer from '@components/AppContainer/AppContainer'

function App () {
  const { t, i18n } = useTranslation()

  // Various loader in a page. This constants declare an area for execution of eache of them
  const importersListLoaderArea = 'importers-loader-area'
  const exportersListLoaderArea = 'exporters-loader-area'
  const exportAllButtonLoaderArea = 'export-all-loader-area'
  const importAllButtonLoaderArea = 'import-all-loader-area'

  // Hooks are used in main App to follow React rules to uses hooks (for example it is not allowed to use a hook inside a function triggered bye an event etc ...)
  const errorHandler = useErrorHandler()

  const [allExporters, setAllExporters] = useState([])
  const [selectedExporters, setSelectedExporters] = useState([])
  RetrieveAllExporters(setAllExporters, errorHandler, exportAllButtonLoaderArea)

  const [allImporters, setAllImporters] = useState([])
  const [selectedImporters, setSelectedImporters] = useState([])
  RetrieveAllImporters(setAllImporters, errorHandler, importAllButtonLoaderArea)

  const [exportId, setExportId] = useState('')
  const [importId, setImportId] = useState('')

  return (
    <AppContainer>
      {isSignedIn()
        ? (
          <ClayLayout.ContainerFluid view>
            <ClayLayout.Row justify='center'>
              <ClayLayout.Col size={6}>
                <ClayLayout.Row justify='start'>
                  <h3 className='sheet-subtitle text-left'>{t('export-process-description')}</h3>
                </ClayLayout.Row>
                <ClayLayout.Row justify='start'>
                  <TaskList title={t('title-exporters')} position='left' datas={allExporters} selectedItems={selectedExporters} setSelectedItemsCallBack={setSelectedExporters} />
                  <LoadingIndicator area={exportersListLoaderArea} />
                </ClayLayout.Row>
                <ClayLayout.Row justify='center'>
                  <ClayButton onClick={() => ExecuteAllExporter(setExportId, errorHandler, exportAllButtonLoaderArea)}>
                    <span className='inline-item inline-item-before'>
                      <ClayIcon className='unstyled' spritemap={spritemap} symbol='play' />
                      <LoadingIndicator area={exportAllButtonLoaderArea} />
                    </span>
                    {t('button-export')}
                  </ClayButton>
                </ClayLayout.Row>
              </ClayLayout.Col>
              <ClayLayout.Col size={6}>
                <ClayLayout.Row justify='start'>
                  <h3 className='sheet-subtitle text-left'>{t('import-process-description')}</h3>
                </ClayLayout.Row>
                <ClayLayout.Row justify='start'>
                  <TaskList title={t('title-importers')} position='right' datas={allImporters} selectedItems={selectedImporters} setSelectedItemsCallBack={setSelectedImporters} />
                  <LoadingIndicator area={importersListLoaderArea} />
                </ClayLayout.Row>
                <ClayLayout.Row justify='center'>
                  <ClayButton onClick={() => ExecuteAllImporter(setImportId, errorHandler, importAllButtonLoaderArea)}>
                    <span className='inline-item inline-item-before'>
                      <ClayIcon className='unstyled' spritemap={spritemap} symbol='play' />
                      <LoadingIndicator area={importAllButtonLoaderArea} />
                    </span>
                    {t('button-import')}
                  </ClayButton>
                </ClayLayout.Row>
              </ClayLayout.Col>
            </ClayLayout.Row>
          </ClayLayout.ContainerFluid>
          )
        : (
          <ClayAlert displayType='warning' title={t('unsigned-alert-message-title')} spritemap={spritemap}>
            {t('unsigned-alert-message-description')}
          </ClayAlert>
          )}
    </AppContainer>
  )
}

function ExecuteAllExporter (updateStateSuccessCallBack, errorHandler, loaderArea) {
  const body = {
    profileId: 'dev',
    exporterNames: null,
    debug: false
  }

  const id = ExecuteAll('/exports', body, errorHandler, loaderArea)

  updateStateSuccessCallBack(id)
}

function ExecuteAllImporter (updateStateSuccessCallBack, errorHandler, loaderArea) {
  const body = {
    profileId: 'dev',
    exporterNames: null,
    debug: false
  }

  const id = ExecuteAll('/imports', body, errorHandler, loaderArea)

  updateStateSuccessCallBack(id)
}

function ExecuteAll (endPoint, body, errorHandler, loaderArea) {
  trackPromise(
    client.post(endPoint, body)
      .then(
        response => {
          const id = response.data
          console.log('Executed with returned id =>' + JSON.stringify(id))
          return id
        },
        error => {
          // Unable to use hooks (handleError()) in function triggered by click!, Limitation of React
          errorHandler(error)
          console.log(error)
        }
      )
    , loaderArea)
}

// Uppercase in function name is mandatory to allow using hooks in function call hierarchy (useState end useEffect)
function RetrieveAllExporters (updateStateSuccessCallBack, errorHandler, loaderArea) {
  RetrieveAll('/exporters', updateStateSuccessCallBack, errorHandler, loaderArea)
}

function RetrieveAllImporters (updateStateSuccessCallBack, errorHandler, loaderArea) {
  RetrieveAll('/importers', updateStateSuccessCallBack, errorHandler, loaderArea)
}

function RetrieveAll (endPoint, updateStateSuccessCallBack, errorHandler, loaderArea) {
  useEffect(() => {
    // TackPromise is used to manage loader waiting for promise execution see react-promise-tracker
    trackPromise(
      client.get(endPoint)
        .then(
          response => {
            const allDatas = response.data.items
            console.log('Received response for API call =>' + JSON.stringify(allDatas))
            updateStateSuccessCallBack(allDatas)
          },
          error => {
            errorHandler(error)
          }
        )
      , loaderArea)
  }, [])
}

export function isSignedIn () {
  if (process.env.NODE_ENV === 'development') {
    return true
  }
  return Liferay().ThemeDisplay.isSignedIn()
}

export function Liferay () {
  return window.Liferay
}

export default App
