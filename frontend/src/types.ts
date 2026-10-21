export type Status = 'ACTIVE' | 'DISABLED'

export type Person = {
  id: number
  name: string
  phoneNumber: string
  emailAddress: string
  status: Status
}

export type Student = Person & {
  studentNumber: string
  photo: string
}

export type Professor = Person & {
  salary: number
}

export type Address = {
  id: number
  street: string
  city: string
  state: string
  zipCode: string
  country: string
  person: Person
}
