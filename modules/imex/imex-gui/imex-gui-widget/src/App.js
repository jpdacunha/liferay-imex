import React from 'react'
import ClayAlert from '@clayui/alert'
import ClayLayout from '@clayui/layout'
import ClayIcon from '@clayui/icon'
import ClayButton from '@clayui/button'
import TaskList from './components/TaskList/TaskList'
import '@clayui/css/lib/css/atlas.css'
import './App.css'
import spritemap from './icons.svg'
import allImporters from './statics/all.importers.json'
import allExporters from './statics/all.exporters.json'

import { useTranslation } from 'react-i18next'

// Don't forget to create a file '.env.local'
// and to add keys & values that we're using in this app
// according to your environment.
import 'dotenv/config'

function App () {
  const { t, i18n } = useTranslation()

  return (
    <div className='App'>
      <div className='container-fluid container-fluid-max-xl container-view'>
        <div className='sheet'>
          <div className='sheet-header'>
            <h2 className='sheet-title'>{t('main-title')}</h2>
            <div className='sheet-text text-justify'>{t('main-description')}</div>
          </div>
          <div className='sheet-section'>
            {isSignedIn()
              ? (
                <ClayLayout.ContainerFluid view>
                  <ClayLayout.Row justify='center'>
                    <ClayLayout.Col size={6}>
                      <ClayLayout.Row justify='start'>
                        <h3 className='sheet-subtitle text-left'>{t('export-process-description')}</h3>
                      </ClayLayout.Row>
                      <ClayLayout.Row justify='start'>
                        <TaskList title={t('title-exporters')} position='left' datas={retrieveAllExporter()} />
                      </ClayLayout.Row>
                      <ClayLayout.Row justify='center'>
                        <ClayButton>
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
                        <TaskList title={t('title-importers')} position='right' datas={retrieveAllImporter()} />
                      </ClayLayout.Row>
                      <ClayLayout.Row justify='center'>
                        <ClayButton>
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
                <ClayAlert displayType='warning' title={t('unsigned-alert-message-title')}>
                  {t('unsigned-alert-message-description')}
                </ClayAlert>
                )}
          </div>
        </div>
      </div>
    </div>
  )
}

function retrieveAllExporter () {
  return allExporters
}

function retrieveAllImporter () {
  return allImporters
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
