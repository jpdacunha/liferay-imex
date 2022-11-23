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

const ImexGui = () => {
	return <React.StrictMode>
          <ErrorBoundary FallbackComponent={ErrorFallbackComponent} onError={(error, componentStack) => { console.log('CATCHED ERROR : message : ' + error + ' | stack : ' + JSON.stringify(componentStack)) }}>
            <App />
          </ErrorBoundary>
        </React.StrictMode>;
};

class WebComponent extends HTMLElement {
  connectedCallback() {
    ReactDOM.render(
      <ImexGui />,
      this
    );
  }
}

const ELEMENT_ID = 'imex-gui-app';

if (!customElements.get(ELEMENT_ID)) {
  console.log('Registering ' + ELEMENT_ID + ' as custom element');
	customElements.define(ELEMENT_ID, WebComponent);
} else {
  console.log('Skipping registration for ' + ELEMENT_ID + ' (already registered)');
} 

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals()
