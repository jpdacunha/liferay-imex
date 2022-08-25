import React from 'react'
import PropTypes from 'prop-types'
import './OptionPanel.scss'
import ClayPanel from '@clayui/panel';
import { useTranslation } from 'react-i18next'
import {ClayCheckbox} from '@clayui/form'
import ClayLayout from '@clayui/layout'
import ProfileSelector from '@components/ProfileSelector/ProfileSelector'

export default function OptionsPanel (props) {

  const { t, i18n } = useTranslation()
  const setDebugModeCallBack = props.setDebugModeCallBack
  const selectedProfileCallBack = props.selectedProfileCallBack
  const errorHandler = props.errorHandler
  const identifier = props.identifier
  const currentProfile = props.currentProfile;

  return (
    <ClayPanel
        displayTitle={t('options')}
        displayType="secondary"
        className={'option-panel option-panel-' + props.position}
    >
        <ClayPanel.Body> 
                <ClayLayout.ContentSection>
                    <ClayLayout.ContentRow padded>
                        <ClayCheckbox label={t('debug-checkbox-label')}  onChange={(e) => handleChange(setDebugModeCallBack, e)} />
                    </ClayLayout.ContentRow>
                    <ClayLayout.ContentRow padded>
                       <ProfileSelector identifier={identifier} errorHandler={errorHandler} selectedItemCallBack={selectedProfileCallBack} selectedProfile={currentProfile}/>
                    </ClayLayout.ContentRow>
                </ClayLayout.ContentSection>
        </ClayPanel.Body>
    </ClayPanel>    
  )
}

const handleChange = (setSelectedItemsCallBack,  event) => {

    let selected = event.target.checked
    console.log('Debug mode : ' + selected)
    setSelectedItemsCallBack(selected)
}

OptionsPanel.propTypes = {
    position: PropTypes.oneOf(['right', 'left']),
    errorHandler: PropTypes.func.isRequired,
    setDebugModeCallBack: PropTypes.func.isRequired,
    selectedProfilesCallBack: PropTypes.func.isRequired,
    identifier:PropTypes.string.isRequired,
    currentProfile:PropTypes.string
}

OptionsPanel.defaultProps = {
    position: 'left',
    errorHandler: {},
    setDebugModeCallBack:  () => {},
    selectedProfilesCallBack:  () => {},
    identifier:"option-panel-",
    currentProfile:""
}
