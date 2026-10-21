import type { Person } from '../../types'
import { request } from './client'

export const peopleApi = {
  list: () => request<Person[]>('/person'),
}
