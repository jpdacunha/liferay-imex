import React from 'react'
import ClayLoadingIndicator from '@clayui/loading-indicator'
// React promise loader is used to track promise state and shos a loader
import { usePromiseTracker } from 'react-promise-tracker'

const LoadingIndicator = props => {
  const { promiseInProgress } = usePromiseTracker({ area: props.area })
  return (
    promiseInProgress && <ClayLoadingIndicator />
  )
}

export default LoadingIndicator
