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
import ErrorMessages from './components/ErrorMessages/ErrorMessages';

function App () {
  const { t, i18n } = useTranslation()

  // Various loader in a page. This constants declare an area for execution of eache of them
  const importersListLoaderArea = 'importers-loader-area'
  const importAllButtonLoaderArea = 'import-all-loader-area'
  const exportersListLoaderArea = 'exporters-loader-area'
  const exportAllButtonLoaderArea = 'export-all-loader-area'

  //Error handler for technical errors (global error boundary)
  const globalErrorHandler = useErrorHandler()

  //Exporters variables definitions
  const [exportId, setExportId] = useState('')
  const [allExporters, setAllExporters] = useState([])
  const [selectedExporters, setSelectedExporters] = useState([])
  RetrieveAllExporters(setAllExporters, exportAllButtonLoaderArea, globalErrorHandler)
  const [exportValidationErrors, setExportValidationErrors] = useState([])

  //Importers variables definitions
  const [importId, setImportId] = useState('')
  const [allImporters, setAllImporters] = useState([])
  const [selectedImporters, setSelectedImporters] = useState([])
  RetrieveAllImporters(setAllImporters, importAllButtonLoaderArea, globalErrorHandler)
  const [importValidationErrors, setImportValidationErrors] = useState([])

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
                <ErrorMessages errorKeys={exportValidationErrors} position='left'/>   
                <ClayLayout.Row justify='start'>
                    <TaskList title={t('title-exporters')} position='left' datas={allExporters} selectedItems={selectedExporters} setSelectedItemsCallBack={setSelectedExporters} />
                    <LoadingIndicator area={exportersListLoaderArea} />
                </ClayLayout.Row>
                <ClayLayout.Row justify='center'>
                  <ClayButton onClick={() => ExecuteExporters(setExportId, exportAllButtonLoaderArea, selectedExporters, globalErrorHandler, setExportValidationErrors)}>
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
                <ErrorMessages errorKeys={importValidationErrors} position='right'/>
                <ClayLayout.Row justify='start'>
                  <TaskList title={t('title-importers')} position='right' datas={allImporters} selectedItems={selectedImporters} setSelectedItemsCallBack={setSelectedImporters} />
                  <LoadingIndicator area={importersListLoaderArea} />
                </ClayLayout.Row>
                <ClayLayout.Row justify='center'>               
                  <ClayButton onClick={() => ExecuteImporters(setImportId, importAllButtonLoaderArea, selectedImporters, globalErrorHandler, setImportValidationErrors)}>
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

function isValidExportersExecution(selectedExporters) {

  let exportValidationErrors = []

  // At least one selected exporter
  const selectedAtLeastOneExporter = selectedExporters && selectedExporters.length > 0;
  if (!selectedAtLeastOneExporter) {
    exportValidationErrors.push('at-least-one-exporter-alert-message')
  }

  return exportValidationErrors

}

function ExecuteExporters (updateStateSuccessCallBack, loaderArea, selectedExporters, errorHandler, updateValidationErrorsListCallBack) {

  const errorsList = isValidExportersExecution(selectedExporters)

  if (errorsList && errorsList.length === 0) {

    //TODO : JDA : manage profil and debug here
    const body = {
      profileId: 'dev',
      exporterNames: selectedExporters,
      debug: false
    }

    console.log('Executing exporters :'  + JSON.stringify(body))

    const id = ExecuteAll('/exports', body, loaderArea, errorHandler)

    updateStateSuccessCallBack(id)

  } else {
    console.log("Aborting execution because form is not properly filled. Errors : " + errorsList instanceof Array)
  }

  updateValidationErrorsListCallBack(errorsList)

}

function isValidImportersExecution(selectedImporters) {

  let importValidationErrors = []

  // At least one selected exporter
  const selectedAtLeastOneImporter = selectedImporters && selectedImporters.length > 0;
  if (!selectedAtLeastOneImporter) {
    importValidationErrors.push('at-least-one-importer-alert-message')
  }

  return importValidationErrors

}

function ExecuteImporters (updateStateSuccessCallBack, loaderArea, selectedImporters, errorHandler, updateValidationErrorsListCallBack) {

  const errorsList = isValidImportersExecution(selectedImporters)

  if (errorsList && errorsList.length === 0) {

    //TODO : JDA : manage profil and debug here
    const body = {
      profileId: 'dev',
      exporterNames: selectedImporters,
      debug: false
    }

    console.log('Executing importers :'  + JSON.stringify(body))

    const id = ExecuteAll('/imports', body, loaderArea, errorHandler)

    updateStateSuccessCallBack(id)

  } else {
    console.log("Aborting execution because form is not properly filled. Errors : " + errorsList instanceof Array)
  }

  updateValidationErrorsListCallBack(errorsList)
  
}

function ExecuteAll (endPoint, body, loaderArea, errorHandler) {

  trackPromise(
    client.post(endPoint, body)
      .then(
        response => {
          const id = response.data
          console.log('Executed with returned id =>' + JSON.stringify(id))
          return id
        },
        error => {
          errorHandler(error)
          console.log(error)
        }
      )
    , loaderArea)
}

// Uppercase in function name is mandatory to allow using hooks in function call hierarchy (useState end useEffect)
function RetrieveAllExporters (updateStateSuccessCallBack, loaderArea, errorHandler) {
  RetrieveAll('/exporters', updateStateSuccessCallBack, loaderArea)
}

function RetrieveAllImporters (updateStateSuccessCallBack, loaderArea, errorHandler) {
  RetrieveAll('/importers', updateStateSuccessCallBack, loaderArea)
}

const RetrieveAll = (endPoint, updateStateSuccessCallBack, loaderArea, errorHandler) => {

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
