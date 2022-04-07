import axios from 'axios'

const localClientParams = {
  // The basURL is not using FQDN in dev in order to make proxy feature available. See package.json for proxy configuration (only in dev)
  baseURL: process.env.REACT_APP_LIFERAY_API_SUFFIX,
  headers: {
    'Content-type': 'application/json'
  },
  auth: {
    username: process.env.REACT_APP_LIFERAY_USER,
    password: process.env.REACT_APP_LIFERAY_PASSWORD
  }
}

const nonDevClientParams = {
  // Use Oauth identification instead of basic auth
  baseURL: process.env.REACT_APP_LIFERAY_HOST + process.env.REACT_APP_LIFERAY_API_SUFFIX,
  headers: {
    'Content-type': 'application/json'
  },
  // TODO : replace with Oauth
  auth: {
    username: process.env.REACT_APP_LIFERAY_USER,
    password: process.env.REACT_APP_LIFERAY_PASSWORD
  }
}

function createCLient () {
  if (process.env.NODE_ENV === 'development') {
    console.log('Creating Rest Client for development ...')
    return axios.create(localClientParams)
  }
  return axios.create(nonDevClientParams)
}

export default createCLient()
