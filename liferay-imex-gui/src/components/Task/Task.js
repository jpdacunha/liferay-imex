import React from 'react'
import PropTypes from 'prop-types'
import ClayList from '@clayui/list'
import ClayLabel from '@clayui/label'
import {ClayCheckbox} from '@clayui/form';
import './Task.scss'
import spritemap from '@images/icons.svg'

import { useTranslation } from 'react-i18next'

const UNKNOWN = 'unkown'

function getSupportedProfilLabelComponent (props, labelText) {
  const profiled = props.profiled

  let profileLabel
  if (profiled) {
    const supportedProfiles = props.supportedProfilesIds
    let formatedProfile = ''
    if (props.profiled) {
      formatedProfile = supportedProfiles.join(', ')
    }

    profileLabel = <ClayLabel displayType='info' spritemap={spritemap} className='imex-label'>{labelText} : {formatedProfile}</ClayLabel>
  }

  return profileLabel
}

const handleChange = (setSelectedItemsCallBack, selectedItems, event) => {
  // this.items = new Set()
  const itemName = event.target.name
  console.log('->' + itemName)
  console.log('(>' + event.target.checked)
  console.log('(:' + JSON.stringify(selectedItems))

  const setOfSelectedItems = new Set(selectedItems)

  if (event.target.checked === true) {
    setOfSelectedItems.add(itemName)
  } else {
    setOfSelectedItems.delete(itemName)
  }
  console.log(setOfSelectedItems)
  setSelectedItemsCallBack(Array.from(setOfSelectedItems))
  console.log(selectedItems)
}

export default function Task (props) {
  const { t, i18n } = useTranslation()
  const labelSupportedProfiles = t('label-supported-profiles-ids')
  const labelPriority = t('label-priority')
  const labelRanking = t('label-ranking')
  const profilLabelComponent = getSupportedProfilLabelComponent(props, labelSupportedProfiles)
  const setSelectedItemsCallBack = props.setSelectedItemsCallBack
  const selectedItems = props.selectedItems
  return (
    <ClayList.Item flex className='task'>
      <ClayList.ItemField>
        <ClayCheckbox name={props.name} onChange={(e) => handleChange(setSelectedItemsCallBack, selectedItems, e)} />
      </ClayList.ItemField>
      <ClayList.ItemField expand>
        <ClayList.ItemTitle>{props.name}</ClayList.ItemTitle>
        <ClayList.ItemText>{props.description}</ClayList.ItemText>
      </ClayList.ItemField>
      <ClayList.ItemField>
        <ClayLabel displayType='success' spritemap={spritemap} className='imex-label'>{labelPriority} : {props.priority}</ClayLabel>
        <ClayLabel displayType='secondary' spritemap={spritemap} className='imex-label'>{labelRanking} : {props.ranking}</ClayLabel>
        {profilLabelComponent}
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
  supportedProfilesIds: PropTypes.array,
  setSelectedItemsCallBack: PropTypes.func.isRequired,
  selectedItems: PropTypes.array
}

Task.defaultProps = {
  description: UNKNOWN,
  name: UNKNOWN,
  priority: 0,
  profiled: false,
  ranking: UNKNOWN,
  supportedProfilesIds: [],
  setSelectedItemsCallBack: () => {},
  selectedItems: []
}
