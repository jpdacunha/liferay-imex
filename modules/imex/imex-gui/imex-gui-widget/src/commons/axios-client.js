import axios from 'axios'
import 'dotenv/config'

const base64credentials = Buffer.from(`${process.env.REACT_APP_LIFERAY_USER}:${process.env.REACT_APP_LIFERAY_PASSWORD}`).toString('base64')

const localClientParams = {
  baseURL: process.env.REACT_APP_LIFERAY_HOST + process.env.REACT_APP_LIFERAY_API_SUFFIX,
  headers: {
    'Content-type': 'application/json'
  },
  auth: {
    username: process.env.REACT_APP_LIFERAY_USER,
    password: process.env.REACT_APP_LIFERAY_PASSWORD
  }
}

export default axios.create(localClientParams)
