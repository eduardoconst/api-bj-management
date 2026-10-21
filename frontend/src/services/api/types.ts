import type { Address, Person, Professor, Student } from '../../types'

export type StudentInput = Omit<Student, 'id'>
export type ProfessorInput = Omit<Professor, 'id'>
export type AddressInput = Omit<Address, 'id' | 'person'> & {
  person: Pick<Person, 'id'>
}
