const { alias } = require('react-app-rewire-alias')

module.exports = function override (config) {
  alias({
    '@components': 'src/components',
    '@images': 'src/images',
    '@commons': 'src/commons'
  })(config)

  return config
}
