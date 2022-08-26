import React, { useState, useEffect } from 'react'
import PropTypes from 'prop-types'
import './ProfilesSelector.scss'
import ClayLayout from '@clayui/layout'
import { useTranslation } from 'react-i18next'
import LoadingIndicator from '@components/LoadingIndicator/LoadingIndicator'
import { trackPromise } from 'react-promise-tracker'
import client from '@commons/axios-client'

export default function ProfileSelector (props) {

  const { t, i18n } = useTranslation()

  //Profiles management
  const errorHandler = props.errorHandler
  const selectedItemCallBack = props.selectedItemCallBack
  const profileLoaderArea = 'profiles-loader-area'
  const identifier = props.identifier
  const selectedProfile = props.selectedProfile

  const [availableProfiles, setAvailableProfiles] = useState([])
  RetrieveProfiles(setAvailableProfiles, profileLoaderArea, errorHandler)
    
  return (
    <div className='profile-selector mt-3'>
      <ClayLayout.ContentSection>
        <ClayLayout.ContentRow>
          <label className='mb-2'>{t('select-profile')} : </label>
        </ClayLayout.ContentRow>
        <ClayLayout.ContentRow>
 
              {availableProfiles && availableProfiles.length > 0 && availableProfiles.map(item => (
                //We use standard markup because ClayRadioGroup does not handle click : maybe a bug 
                <div key={'key-' + item.profileId + "-" + identifier} className="ml-2">
                  <input type="radio" value={item.profileId} id={item.profileId} onChange={(e) => handleChange(selectedItemCallBack, selectedProfile, e)} name={"profile-radio-" + identifier} />

                  { item.profileId === selectedProfile ? (
                    <label htmlFor={item.name} className="selected ml-1">{item.name}</label>
                  )
                  : (
                    <label htmlFor={item.name} className="ml-1">{item.name}</label>
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

const RetrieveProfiles = (setAvailableProfilesCallBack, loaderArea, errorHandler) => {

  useEffect(() => {
    // TackPromise is used to manage loader waiting for promise execution see react-promise-tracker
    trackPromise(
      client.get('/profiles')
        .then(
          response => {
            const allDatas = response.data.items
            console.log('Received response for API call =>' + JSON.stringify(allDatas))
            setAvailableProfilesCallBack(allDatas);
          },
          error => {
            errorHandler(error)
          }
        )
      , loaderArea)
  }, [])

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
