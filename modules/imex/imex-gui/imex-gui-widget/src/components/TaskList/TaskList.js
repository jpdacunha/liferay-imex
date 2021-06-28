import React from 'react';
import PropTypes from 'prop-types'
import ClayList from '@clayui/list'
import Task from '../Task/Task'
import './TaskList.scss'
// import { useTranslation } from 'react-i18next'

function handlePartialLaunch (bundleName) {
  console.log('handlePartialLaunch : ' + bundleName)
}

export default function TaskList (props) {
  // const { t, i18n } = useTranslation()
  console.log('>>>>>>>>>>> Received datas : ' + JSON.stringify(props.datas.items))

  return (
    <ClayList className={'task-list task-list-' + props.position}>
      <ClayList.Header>{props.title}</ClayList.Header>
      {props.datas.items.map(item => (
        <Task key={item.name} description={item.description} name={item.name} priority={item.priority} profiled={item.profiled} ranking={item.ranking} supportedProfilesIds={item.supportedProfilesIds} launchAction={() => handlePartialLaunch(item.name)} />
      ))}
    </ClayList>
  )
}

TaskList.propTypes = {
  title: PropTypes.string,
  position: PropTypes.oneOf(['right', 'left']),
  datas: PropTypes.object
}

TaskList.defaultProps = {
  title: '',
  position: 'left',
  datas: {}
}
