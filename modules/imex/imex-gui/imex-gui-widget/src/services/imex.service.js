import client from '../commons/http-common'

class ImexService {
  getExporters () {
    return client.get('exporters')
  }
}

export default new ImexService()
