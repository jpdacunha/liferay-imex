import React from 'react'
import PropTypes from 'prop-types'
import ClayList from '@clayui/list'
import ClayAlert from '@clayui/alert'
import Task from '@components/Task/Task'
import './TaskList.scss'
import spritemap from '@images/icons.svg'
import { useTranslation } from 'react-i18next'

function handlePartialLaunch (bundleName) {
  console.log('handlePartialLaunch : ' + bundleName)
}

export default function TaskList (props) {
  const { t, i18n } = useTranslation()
  const arrayOfDatas = props.datas
  console.log('Received datas : ' + JSON.stringify(arrayOfDatas))

  if (arrayOfDatas && arrayOfDatas.length > 0) {
    return (
      <ClayList className={'task-list task-list-' + props.position}>
        <ClayList.Header>{props.title}</ClayList.Header>
        {arrayOfDatas && arrayOfDatas.map(item => (
          <Task key={item.name} description={item.description} name={item.name} priority={item.priority} profiled={item.profiled} ranking={item.ranking} supportedProfilesIds={item.supportedProfilesIds} launchAction={() => handlePartialLaunch(item.name)} />
        ))}
      </ClayList>
    )
  } else {
    return (
      <ClayAlert displayType='info' className='imex-alert' spritemap={spritemap}>
        {t('empty-datas-message')}
      </ClayAlert>
    )
  }
}

TaskList.propTypes = {
  title: PropTypes.string,
  position: PropTypes.oneOf(['right', 'left']),
  datas: PropTypes.array
}

TaskList.defaultProps = {
  title: '',
  position: 'left',
  datas: []
}
