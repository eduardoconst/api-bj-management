import type { Professor } from '../../types'
import { deleteRequest, jsonRequest, request } from './client'
import type { ProfessorInput } from './types'

export const professorsApi = {
  list: () => request<Professor[]>('/professor'),
  create: (payload: ProfessorInput) => jsonRequest<Professor>('/professor', 'POST', payload),
  update: (id: number, payload: ProfessorInput) =>
    jsonRequest<Professor>(`/professor/${id}`, 'PUT', payload),
  delete: (id: number) => deleteRequest(`/professor/${id}`),
}
