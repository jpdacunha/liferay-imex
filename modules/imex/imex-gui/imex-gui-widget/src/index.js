import React from 'react'
import ReactDOM from 'react-dom'
import './index.css'
import App from './App'
import reportWebVitals from './reportWebVitals'
import '@commons/i18n'
import AppContainer from '@components/AppContainer/AppContainer'
import { ErrorBoundary } from 'react-error-boundary'
import ClayAlert from '@clayui/alert'
import { useTranslation } from 'react-i18next'
import spritemap from '@images/icons.svg'

const ErrorFallbackComponent = ({ error, componentStack, resetErrorBoundary }) => {
  const { t, i18n } = useTranslation()
  return (
    <AppContainer>
      <ClayAlert displayType='danger' title={t('technical-alert-message-description')} spritemap={spritemap}>
        {error.message}
      </ClayAlert>
    </AppContainer>
  )
}

ReactDOM.render(
  <React.StrictMode>
    <ErrorBoundary FallbackComponent={ErrorFallbackComponent} onError={(error, componentStack) => { console.log('CATCHED ERROR : message : ' + error + ' | stack : ' + JSON.stringify(componentStack)) }}>
      <App />
    </ErrorBoundary>
  </React.StrictMode>,
  document.getElementById('root')
)

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals()
