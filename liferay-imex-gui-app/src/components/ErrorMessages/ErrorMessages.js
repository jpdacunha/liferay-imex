import React from 'react'
import PropTypes from 'prop-types'
import ClayAlert from '@clayui/alert'
import ClayLayout from '@clayui/layout'
import spritemap from '@images/icons.svg'
import { useTranslation } from 'react-i18next'
import './ErrorMessages.scss'

const ErrorMessages = props => {
  const { t, i18n } = useTranslation()
  const errors = props.errorKeys

  if (errors.length > 0) {
    console.log("Receiving validation errors to display : " + errors)
  } else {
    //console.log("No errors to display");  
  }
  
  return (
    <ClayLayout.Row justify='center'>
        {errors && errors.length > 0 && errors.map(item => (
            <ClayAlert key={'key-' + item} displayType='danger' title={t('validation-error-label')} spritemap={spritemap} className={'errors-messages-container-' + props.position}>
                {t(item)}
            </ClayAlert>
        ))}
    </ClayLayout.Row>
  )
}

ErrorMessages.propTypes = {
    errorKeys: PropTypes.array,
    position: PropTypes.oneOf(['right', 'left']),
}

ErrorMessages.defaultProps = {
    errorKeys: [],
    position: 'left'
}

export default ErrorMessages