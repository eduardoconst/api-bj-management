import type { Student } from '../../types'
import { deleteRequest, jsonRequest, request } from './client'
import type { StudentInput } from './types'

export const studentsApi = {
  list: () => request<Student[]>('/student'),
  create: (payload: StudentInput) => jsonRequest<Student>('/student', 'POST', payload),
  update: (id: number, payload: StudentInput) =>
    jsonRequest<Student>(`/student/${id}`, 'PUT', payload),
  delete: (id: number) => deleteRequest(`/student/${id}`),
}
