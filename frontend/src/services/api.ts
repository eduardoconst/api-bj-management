import { addressesApi } from './api/addresses'
import { peopleApi } from './api/people'
import { professorsApi } from './api/professors'
import { studentsApi } from './api/students'

export type { AddressInput, ProfessorInput, StudentInput } from './api/types'

export const api = {
  listStudents: studentsApi.list,
  createStudent: studentsApi.create,
  updateStudent: studentsApi.update,
  deleteStudent: studentsApi.delete,

  listProfessors: professorsApi.list,
  createProfessor: professorsApi.create,
  updateProfessor: professorsApi.update,
  deleteProfessor: professorsApi.delete,

  listPeople: peopleApi.list,

  listAddresses: addressesApi.list,
  createAddress: addressesApi.create,
  updateAddress: addressesApi.update,
  deleteAddress: addressesApi.delete,
}
