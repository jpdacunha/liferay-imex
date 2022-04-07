import React from 'react'
import PropTypes from 'prop-types'
import './ProfilesSelector.scss'
import { ClayRadio, ClayRadioGroup } from '@clayui/form'

export default function ProfileSelector (props) {
  const onSelectCallBack = props.onSelectCallBack
  const selectedValue = props.selectedValue

  return (
    <ClayRadioGroup
      inline
      onSelectedValueChange={val => onSelectCallBack(val)}
      selectedValue={selectedValue}
    >
      <ClayRadio label='One' value='one' />
      <ClayRadio label='Two' value='two' />
      <ClayRadio label='Three' value='three' />
    </ClayRadioGroup>
  )
}

ProfileSelector.propTypes = {
  onSelectCallBack: PropTypes.func,
  selectedValue: PropTypes.string
}

ProfileSelector.defaultProps = {
  onSelectCallBack: () => {},
  selectedValue: ''
}
