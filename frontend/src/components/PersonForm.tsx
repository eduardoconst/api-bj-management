import { useState, type FormEvent } from 'react'
import type { Professor, Status, Student } from '../types'

type StudentInput = Omit<Student, 'id'>
type ProfessorInput = Omit<Professor, 'id'>

type PersonFormProps =
  | {
      type: 'STUDENT'
      title: string
      initialData?: Student
      submitting: boolean
      onCancel: () => void
      onSubmit: (values: StudentInput) => void
    }
  | {
      type: 'PROFESSOR'
      title: string
      initialData?: Professor
      submitting: boolean
      onCancel: () => void
      onSubmit: (values: ProfessorInput) => void
    }

export function PersonForm(props: PersonFormProps) {
  const [name, setName] = useState(props.initialData?.name ?? '')
  const [emailAddress, setEmailAddress] = useState(props.initialData?.emailAddress ?? '')
  const [phoneNumber, setPhoneNumber] = useState(props.initialData?.phoneNumber ?? '')
  const [status, setStatus] = useState<Status>(props.initialData?.status ?? 'ACTIVE')
  const [studentNumber, setStudentNumber] = useState(
    props.type === 'STUDENT' ? props.initialData?.studentNumber ?? '' : ''
  )
  const [photo, setPhoto] = useState(
    props.type === 'STUDENT' ? props.initialData?.photo ?? '' : ''
  )
  const [salary, setSalary] = useState(
    props.type === 'PROFESSOR' && props.initialData?.salary !== undefined
      ? String(props.initialData.salary)
      : ''
  )

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    if (props.type === 'STUDENT') {
      props.onSubmit({
        name,
        emailAddress,
        phoneNumber,
        status,
        studentNumber,
        photo,
      })
      return
    }

    props.onSubmit({
      name,
      emailAddress,
      phoneNumber,
      status,
      salary: Number(salary),
    })
  }

  return (
    <form className="form" onSubmit={handleSubmit}>
      <h2>{props.title}</h2>

      <label>
        Nome
        <input
          value={name}
          onChange={(event) => setName(event.target.value)}
          required
        />
      </label>

      <label>
        E-mail
        <input
          type="email"
          value={emailAddress}
          onChange={(event) => setEmailAddress(event.target.value)}
          required
        />
      </label>

      <label>
        Telefone
        <input
          value={phoneNumber}
          onChange={(event) => setPhoneNumber(event.target.value)}
          required
        />
      </label>

      <label>
        Status
        <select
          value={status}
          onChange={(event) => setStatus(event.target.value as Status)}
        >
          <option value="ACTIVE">ACTIVE</option>
          <option value="DISABLED">DISABLED</option>
        </select>
      </label>

      {props.type === 'STUDENT' && (
        <>
          <label>
            Matrícula
            <input
              value={studentNumber}
              onChange={(event) => setStudentNumber(event.target.value)}
              required
            />
          </label>

          <label>
            Foto (referência textual)
            <input
              value={photo}
              onChange={(event) => setPhoto(event.target.value)}
              required
            />
          </label>
        </>
      )}

      {props.type === 'PROFESSOR' && (
        <label>
          Salário
          <input
            type="number"
            value={salary}
            onChange={(event) => setSalary(event.target.value)}
            min="0"
            step="0.01"
            required
          />
        </label>
      )}

      <div className="form-actions">
        <button type="button" className="secondary" onClick={props.onCancel}>
          Cancelar
        </button>
        <button type="submit" className="primary" disabled={props.submitting}>
          {props.submitting ? 'Salvando...' : 'Salvar'}
        </button>
      </div>
    </form>
  )
}
