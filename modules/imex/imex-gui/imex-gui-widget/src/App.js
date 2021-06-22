import React from 'react'
import ClayAlert from '@clayui/alert'
import ClayLayout from '@clayui/layout'
import '@clayui/css/lib/css/atlas.css'
import './App.css'

import { useTranslation } from 'react-i18next'

// Don't forget to create a file '.env.local'
// and to add keys & values that we're using in this app
// according to your environment.
import 'dotenv/config'

function App () {
  const { t, i18n } = useTranslation()
  const alertTitle = 'Warning :'
  const alertMessage = 'You need to sign in to see this content.'

  return (
    <div className='App'>
      <div className='container'>
        {isSignedIn()
          ? (
            <ClayLayout.ContainerFluid view>
              <ClayLayout.Row justify='center'>
                <ClayLayout.Col size={6}>
                  {t('Welcome to React')}
                </ClayLayout.Col>
                <ClayLayout.Col size={6}>
                  One of two columns
                </ClayLayout.Col>
              </ClayLayout.Row>
            </ClayLayout.ContainerFluid>
            )
          : (
            <ClayAlert displayType='warning' title={alertTitle}>
              {alertMessage}
            </ClayAlert>
            )}
      </div>
    </div>
  )
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
