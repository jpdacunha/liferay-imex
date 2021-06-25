import React from 'react';
import PropTypes from 'prop-types'
import ClayList from '@clayui/list'
import Task from '../Task/Task'
import './TaskList.scss'
// import { useTranslation } from 'react-i18next'

export function handlePartialLaunch (bundleName) {
  console.log('handlePartialLaunch : ' + bundleName)
}

export default function TaskList (props) {
  // const { t, i18n } = useTranslation()
  const sample = ['dev', 'prod']

  return (
    <ClayList className={'task-list task-list-' + props.position}>
      <ClayList.Header>{props.title}</ClayList.Header>
      <Task description='Description ' name='Nom' priority='1' profiled='true' ranking='100' supportedProfilesIds={sample} launchAction={() => this.handlePartialLaunch('My Name')} />
    </ClayList>
  )
}

TaskList.propTypes = {
  title: PropTypes.string,
  position: PropTypes.oneOf(['right', 'left'])
}

TaskList.defaultProps = {
  title: '',
  position: 'left'
}
