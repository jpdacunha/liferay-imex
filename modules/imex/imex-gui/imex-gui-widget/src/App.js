import React, { useState, useEffect } from 'react'
import ClayAlert from '@clayui/alert'
import ClayLayout from '@clayui/layout'
import ClayIcon from '@clayui/icon'
import ClayButton from '@clayui/button'
import TaskList from './components/TaskList/TaskList'
import '@clayui/css/lib/css/atlas.css'
import './App.css'
import spritemap from './icons.svg'
import { trackPromise } from 'react-promise-tracker'
import client from './commons/axios-client'
import { useTranslation } from 'react-i18next'
import LoadingIndicator from './components/LoadingIndicator/LoadingIndicator'
import { useErrorHandler } from 'react-error-boundary'
import AppContainer from './components/AppContainer/AppContainer'

// Don't forget to create a file '.env.local'
// and to add keys & values that we're using in this app
// according to your environment.
import 'dotenv/config'

function App () {
  const { t, i18n } = useTranslation()
  const allExporters = RetrieveAllExporters()
  const allImporters = RetrieveAllImporters()

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
                  <TaskList title={t('title-exporters')} position='left' datas={allExporters} />
                  <LoadingIndicator />
                </ClayLayout.Row>
                <ClayLayout.Row justify='center'>
                  <ClayButton onClick={executeAllExporter}>
                    <span className='inline-item inline-item-before'>
                      <ClayIcon className='unstyled' spritemap={spritemap} symbol='play' />
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
                  <TaskList title={t('title-importers')} position='right' datas={allImporters} />
                  <LoadingIndicator />
                </ClayLayout.Row>
                <ClayLayout.Row justify='center'>
                  <ClayButton onClick={executeAllImporter}>
                    <span className='inline-item inline-item-before'>
                      <ClayIcon className='unstyled' spritemap={spritemap} symbol='play' />
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

function executeAllExporter () {
  console.log('executeAllExporter')
}

function executeAllImporter () {
  console.log('executeAllImporter')
}

// Uppercase in function name is mandatory to allow using hooks in function call hierarchy (useState end useEffect)
function RetrieveAllExporters () {
  const [exporters, setExporters] = useState([])
  RetrieveAll('exporters', setExporters)
  return exporters
}

function RetrieveAllImporters () {
  const [importers, setImporters] = useState([])
  RetrieveAll('importers', setImporters)
  return importers
}

const RetrieveAll = (endPoint, updateStateSuccessCallBack) => {
  const handleError = useErrorHandler()

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
            handleError(error)
          }
        )
    )
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
