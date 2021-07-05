import React from 'react'
import { useTranslation } from 'react-i18next'

const AppContainer = props => {
  const { t, i18n } = useTranslation()
  return (
    <div className='container-fluid container-fluid-max-xl container-view'>
      <div className='sheet'>
        <div className='sheet-header'>
          <h2 className='sheet-title'>{t('main-title')}</h2>
          <div className='sheet-text text-justify'>{t('main-description')}</div>
        </div>
        <div className='sheet-section'>
          {props.children}
        </div>
      </div>
    </div>

  )
}

export default AppContainer
