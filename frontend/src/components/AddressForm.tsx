import { useState, type FormEvent } from 'react'
import type { Address, Person } from '../types'

type AddressFormProps = {
  title: string
  people: Array<{ id: number; label: string }>
  initialData?: Address
  submitLabel?: string
  submitting: boolean
  onCancel: () => void
  onSubmit: (values: Omit<Address, 'id' | 'person'> & { person: Pick<Person, 'id'> }) => void
}

export function AddressForm({
  title,
  people,
  initialData,
  submitLabel,
  submitting,
  onCancel,
  onSubmit,
}: AddressFormProps) {
  const [street, setStreet] = useState(initialData?.street ?? '')
  const [city, setCity] = useState(initialData?.city ?? '')
  const [state, setState] = useState(initialData?.state ?? '')
  const [zipCode, setZipCode] = useState(initialData?.zipCode ?? '')
  const [country, setCountry] = useState(initialData?.country ?? '')
  const [personId, setPersonId] = useState(
    initialData?.person?.id !== undefined ? String(initialData.person.id) : ''
  )

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    onSubmit({
      street,
      city,
      state,
      zipCode,
      country,
      person: {
        id: Number(personId),
      },
    })
  }

  return (
    <form className="form" onSubmit={handleSubmit}>
      <h2>{title}</h2>

      <label>
        Rua
        <input value={street} onChange={(event) => setStreet(event.target.value)} required />
      </label>

      <label>
        Cidade
        <input value={city} onChange={(event) => setCity(event.target.value)} required />
      </label>

      <label>
        Estado
        <input value={state} onChange={(event) => setState(event.target.value)} required />
      </label>

      <label>
        CEP
        <input value={zipCode} onChange={(event) => setZipCode(event.target.value)} required />
      </label>

      <label>
        País
        <input value={country} onChange={(event) => setCountry(event.target.value)} required />
      </label>

      <label>
        Pessoa relacionada (ID)
        <select
          value={personId}
          onChange={(event) => setPersonId(event.target.value)}
          required
        >
          <option value="">Selecione</option>
          {people.map((person) => (
            <option key={person.id} value={person.id}>
              {person.label}
            </option>
          ))}
        </select>
      </label>

      <div className="form-actions">
        <button type="button" className="secondary" onClick={onCancel}>
          Cancelar
        </button>
        <button type="submit" className="primary" disabled={submitting}>
          {submitting ? 'Salvando...' : submitLabel ?? 'Salvar'}
        </button>
      </div>
    </form>
  )
}
