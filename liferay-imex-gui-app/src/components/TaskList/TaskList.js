import React from 'react'
import PropTypes from 'prop-types'
import ClayList from '@clayui/list'
import ClayAlert from '@clayui/alert'
import {ClayCheckbox} from '@clayui/form';
import Task from '@components/Task/Task'
import './TaskList.scss'
import spritemap from '@images/icons.svg'
import { useTranslation } from 'react-i18next'

export default function TaskList (props) {
  const { t, i18n } = useTranslation()
  const arrayOfDatas = props.datas

  if (arrayOfDatas && arrayOfDatas.length > 0) {

    const setSelectedItemsCallBack = props.setSelectedItemsCallBack
    const selectedItems = props.selectedItems

    const arrayOfDatasNames = arrayOfDatas.map(item => item.name)

    const allSelected = arrayOfDatasNames.every(r => selectedItems.includes(r))

    return (
      <ClayList className={'task-list task-list-' + props.position}>

        <ClayList.Item flex className='task-header'>
          <ClayList.ItemField>
            <ClayCheckbox name='select' onChange={(e) => handleChange(setSelectedItemsCallBack,  arrayOfDatasNames, e)} checked={allSelected}/>
          </ClayList.ItemField>
          <ClayList.ItemField expand>
             {props.title}
          </ClayList.ItemField>

        </ClayList.Item>
        {arrayOfDatas && arrayOfDatas.map(item => (
          <Task key={item.name} description={item.description} name={item.name} priority={item.priority} profiled={item.profiled} ranking={item.ranking} supportedProfilesIds={item.supportedProfilesIds} selectedItems={selectedItems} setSelectedItemsCallBack={setSelectedItemsCallBack} />
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

const handleChange = (setSelectedItemsCallBack, items, event) => {
  
  let updatedSelectedItems = [...items]

  if (event.target.checked === true) {
    console.log('Select ALL items')
    {items && items.map(item => (
      updatedSelectedItems.push(item.name)
    ))}
  } else {
    console.log('Unselect ALL items')
    updatedSelectedItems = []
  }

  console.log('updatedSelectedItems :' + JSON.stringify(updatedSelectedItems))
  setSelectedItemsCallBack(updatedSelectedItems)

}

TaskList.propTypes = {
  title: PropTypes.string,
  position: PropTypes.oneOf(['right', 'left']),
  datas: PropTypes.array,
  selectedItems: PropTypes.array,
  setSelectedItemsCallBack: PropTypes.func
}

TaskList.defaultProps = {
  title: '',
  position: 'left',
  datas: [],
  selectedItems: [],
  setSelectedItemsCallBack: () => {}
}
