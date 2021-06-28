import axios from 'axios'
import 'dotenv/config'

export function client () {
  const user = process.env.REACT_APP_LIFERAY_USER
  const password = process.env.REACT_APP_LIFERAY_PASSWORD

  const endpoint = process.env.REACT_APP_LIFERAY_HOST + process.env.REACT_APP_LIFERAY_API_SUFFIX

  return axios.create({
    baseURL: endpoint,
    headers: {
      'Content-type': 'application/json'
    },
    auth: {
      username: user,
      password: password
    }
  })
}

export default client
