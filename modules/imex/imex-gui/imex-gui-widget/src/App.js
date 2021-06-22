import React from 'react'
import ClayAlert from '@clayui/alert'
import '@clayui/css/lib/css/atlas.css'
import './App.css'

// Don't forget to create a file '.env.local'
// and to add keys & values that we're using in this app
// according to your environment.
import 'dotenv/config'

function App () {
  const alertTitle = 'Warning :'
  const alertMessage = 'You need to sign in to see this content.'

  return (
    <div className='App'>
      <div className='container'>
        {isSignedIn()
          ? (
            <div>
              <h1 className='text-center mb-4'>Blogs</h1>
            </div>
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
