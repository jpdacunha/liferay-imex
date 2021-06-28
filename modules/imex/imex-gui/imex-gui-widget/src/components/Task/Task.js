import React from 'react'
import PropTypes from 'prop-types'
import ClayList from '@clayui/list'
import ClayIcon from '@clayui/icon'
import ClaySticker from '@clayui/sticker'
import ClayLabel from '@clayui/label'
import ClayButton from '@clayui/button'
import './Task.scss'
import spritemap from '../../icons.svg'

import { useTranslation } from 'react-i18next'

const UNKNOWN = 'unkown'

export function getSupportedProfilLabelComponent (props, labelText) {
  const profiled = props.profiled

  let profileLabel
  if (profiled) {
    const supportedProfiles = props.supportedProfilesIds
    let formatedProfile = ''
    if (props.profiled) {
      formatedProfile = supportedProfiles.join(', ')
    }

    profileLabel = <ClayLabel displayType='info' spritemap={spritemap}>{labelText} : {formatedProfile}</ClayLabel>
  }

  return profileLabel
}

export default function Task (props) {
  const { t, i18n } = useTranslation()
  const labelSupportedProfiles = t('label-supported-profiles-ids')
  const labelPriority = t('label-priority')
  const labelRanking = t('label-ranking')
  const labelRun = t('button-label-run')
  const profilLabelComponent = getSupportedProfilLabelComponent(props, labelSupportedProfiles)
  const actionFunction = props.launchAction

  return (
    <ClayList.Item flex className='task'>
      <ClayList.ItemField>
        <ClaySticker displayType='secondary'>
          <ClayIcon spritemap={spritemap} symbol='angle-right-small' />
        </ClaySticker>
      </ClayList.ItemField>
      <ClayList.ItemField expand>
        <ClayList.ItemTitle>{props.name}</ClayList.ItemTitle>
        <ClayList.ItemText>{props.description}</ClayList.ItemText>
      </ClayList.ItemField>
      <ClayList.ItemField>
        <ClayLabel displayType='success' spritemap={spritemap}>{labelPriority} : {props.priority}</ClayLabel>
        <ClayLabel displayType='secondary' spritemap={spritemap}>{labelRanking} : {props.ranking}</ClayLabel>
        {profilLabelComponent}
      </ClayList.ItemField>
      <ClayList.ItemField>
        <ClayButton displayType='secondary' onClick={actionFunction}>
          <span className='inline-item inline-item-before'>
            <ClayIcon className='unstyled' spritemap={spritemap} symbol='play' />
          </span>
          {labelRun}
        </ClayButton>
      </ClayList.ItemField>
    </ClayList.Item>
  )
}

Task.propTypes = {
  description: PropTypes.string,
  name: PropTypes.string,
  priority: PropTypes.number,
  profiled: PropTypes.bool,
  ranking: PropTypes.string,
  supportedProfilesIds: PropTypes.array
}

Task.defaultProps = {
  description: UNKNOWN,
  name: UNKNOWN,
  priority: 0,
  profiled: false,
  ranking: UNKNOWN,
  supportedProfilesIds: []
}
