import React from 'react'
import PropTypes from 'prop-types'
import ClayList from '@clayui/list'
import ClayIcon from '@clayui/icon'
import ClaySticker from '@clayui/sticker'
import ClayLabel from '@clayui/label'
import './Task.scss'
import spritemap from '../../icons.svg'

import { useTranslation } from 'react-i18next'

const UNKNOWN = 'unkown'

export function getSupportedProfilLabelComponent (props, labelText) {
  // TODO JDA : Remove boolean as String value here
  const profiled = (props.profiled === 'true')

  let profileLabel
  if (profiled) {
    const supportedProfiles = props.supportedProfilesIds
    let formatedProfile = ''
    if (props.profiled) {
      formatedProfile = supportedProfiles.join(', ')
    }

    profileLabel = <ClayLabel displayType='success' spritemap={spritemap}>{labelText} : {formatedProfile}</ClayLabel>
  }

  return profileLabel
}

export default function Task (props) {
  const { t, i18n } = useTranslation()
  const labelText = t('heading-table-supported-profiles-ids')
  const profilLabelComponent = getSupportedProfilLabelComponent(props, labelText)

  return (
    <div className='task'>
      <ClayList.Item flex>
        <ClayList.ItemField expand>
          <ClaySticker displayType='secondary'>
            <ClayIcon spritemap={spritemap} symbol='play' />
          </ClaySticker>
        </ClayList.ItemField>
        <ClayList.ItemField expand>
          <ClayList.ItemTitle>{props.name}</ClayList.ItemTitle>
          <ClayList.ItemText>{props.description}</ClayList.ItemText>
          {profilLabelComponent}
        </ClayList.ItemField>
      </ClayList.Item>
    </div>
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
