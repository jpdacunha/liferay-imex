import React from 'react';
import PropTypes from 'prop-types'
import ClayList from '@clayui/list'
import Task from '../Task/Task'
import './TaskList.scss'
import { useTranslation } from 'react-i18next'

export default function TaskList (props) {
  const { t, i18n } = useTranslation()
  const sample = ['dev', 'prod']

  return (
    <div className='task-list'>
      <ClayList>
        <ClayList.Header>{props.title}</ClayList.Header>
        <Task description='Description ' name='Nom' priority='1' profiled='true' ranking='100' supportedProfilesIds={sample} />
      </ClayList>
    </div>
  )
}

TaskList.propTypes = {
  title: PropTypes.string
}

TaskList.defaultProps = {
  title: ''
}
