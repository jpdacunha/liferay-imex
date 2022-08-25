import React, { useState, useEffect } from 'react'
import PropTypes from 'prop-types'
import './ProfilesSelector.scss'
import ClayLayout from '@clayui/layout'
import { useTranslation } from 'react-i18next'
import LoadingIndicator from '@components/LoadingIndicator/LoadingIndicator'

export default function ProfileSelector (props) {

  const { t, i18n } = useTranslation()

  //Profiles management
  const errorHandler = props.errorHandler
  const selectedItemCallBack = props.selectedItemCallBack
  const profileLoaderArea = 'profiles-loader-area'
  const availableProfiles = RetrieveProfiles(profileLoaderArea, errorHandler)
  const identifier = props.identifier
  const selectedProfile = props.selectedProfile
    
  return (
    <div className='profile-selector mt-3'>
      <ClayLayout.ContentSection>
        <ClayLayout.ContentRow>
          <label className='mb-2'>{t('select-profile')} : </label>
        </ClayLayout.ContentRow>
        <ClayLayout.ContentRow>
 
              {availableProfiles && availableProfiles.length > 0 && availableProfiles.map(item => (
                //We use standard markup because ClayRadioGroup does not handle click : maybe a bug 
                <div key={'key-' + item + "-" + identifier} className="ml-2">
                  <input type="radio" value={item} id={item} onChange={(e) => handleChange(selectedItemCallBack, selectedProfile, e)} name={"profile-radio-" + identifier} />

                  { item === selectedProfile ? (
                    <label htmlFor={item} className="selected ml-1">{item}</label>
                  )
                  : (
                    <label htmlFor={item} className="ml-1">{item}</label>
                  )}
                  
                </div>
              ))}

        </ClayLayout.ContentRow>
        <LoadingIndicator area={profileLoaderArea} />
      </ClayLayout.ContentSection>
    </div>
  )
}

const handleChange = (setSelectedItemCallBack, currentSelectedProfile,  event) => {

  console.log("Received event : " + event.target.value);
  let selectedProfileValue = event.target.value;
  setSelectedItemCallBack(selectedProfileValue);
  
}

const RetrieveProfiles = (loaderArea, errorHandler) => {

  //TODO : JDA WS call here instead of this
  const mockArray = [];
  mockArray.push("dev");
  mockArray.push("int");
  mockArray.push("test");
  mockArray.push("preprod");
  mockArray.push("prod");

  return mockArray;

}

ProfileSelector.propTypes = {
  errorHandler: PropTypes.func.isRequired,
  selectedItemsCallBack: PropTypes.func.isRequired,
  identifier:PropTypes.string.isRequired,
  selectedProfile:PropTypes.string
}

ProfileSelector.defaultProps = {
  errorHandler: {},
  selectedItemsCallBack:  () => {},
  identifier:"profile-radio-",
  selectedProfile:""
}
