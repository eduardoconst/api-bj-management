import { useCallback, useEffect, useMemo, useState } from 'react'
import './App.css'
import { AddressForm } from './components/AddressForm'
import { Modal } from './components/Modal'
import { PersonDetails } from './components/PersonDetails'
import { PersonForm } from './components/PersonForm'
import { api } from './services/api'
import type {
  AddressInput,
  ProfessorInput,
  StudentInput,
} from './services/api'
import type { Address, Person, Professor, Student } from './types'

type PersonType = 'STUDENT' | 'PROFESSOR'
type ScreenView = 'PEOPLE' | 'ADDRESSES'

type UnifiedPersonRow = {
  id: number
  name: string
  phoneNumber: string
  emailAddress: string
  status: string
  personType: PersonType
  studentNumber?: string
  salary?: number
  addressText: string
}

type ModalState =
  | { kind: 'none' }
  | { kind: 'create-student' }
  | { kind: 'create-professor' }
  | { kind: 'create-address' }
  | { kind: 'edit-address'; record: Address }
  | { kind: 'view-student'; record: Student }
  | { kind: 'view-professor'; record: Professor }
  | { kind: 'edit-student'; record: Student }
  | { kind: 'edit-professor'; record: Professor }

function App() {
  const [students, setStudents] = useState<Student[]>([])
  const [professors, setProfessors] = useState<Professor[]>([])
  const [addresses, setAddresses] = useState<Address[]>([])
  const [people, setPeople] = useState<Person[]>([])
  const [search, setSearch] = useState('')
  const [loading, setLoading] = useState(true)
  const [submitting, setSubmitting] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [feedback, setFeedback] = useState<string | null>(null)
  const [modal, setModal] = useState<ModalState>({ kind: 'none' })
  const [activeView, setActiveView] = useState<ScreenView>('PEOPLE')

  const loadData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const [studentList, professorList, addressList, personList] =
        await Promise.all([
          api.listStudents(),
          api.listProfessors(),
          api.listAddresses(),
          api.listPeople(),
        ])

      setStudents(studentList)
      setProfessors(professorList)
      setAddresses(addressList)
      setPeople(personList)
    } catch (loadError) {
      setError(
        loadError instanceof Error
          ? loadError.message
          : 'Não foi possível carregar os dados.'
      )
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    void loadData()
  }, [loadData])

  const addressByPerson = useMemo(() => {
    const map = new Map<number, string[]>()

    for (const address of addresses) {
      const personId = address.person?.id
      if (typeof personId !== 'number') {
        continue
      }

      const summary = [address.street, `${address.city}/${address.state}`]
        .filter(Boolean)
        .join(' - ')

      const current = map.get(personId) ?? []
      if (summary.length > 0) {
        current.push(summary)
      }
      map.set(personId, current)
    }

    return map
  }, [addresses])

  const rows = useMemo<UnifiedPersonRow[]>(() => {
    const studentRows: UnifiedPersonRow[] = students.map((student) => ({
      id: student.id,
      name: student.name ?? '',
      phoneNumber: student.phoneNumber ?? '',
      emailAddress: student.emailAddress ?? '',
      status: student.status ?? '',
      personType: 'STUDENT',
      studentNumber: student.studentNumber ?? '',
      addressText: (addressByPerson.get(student.id) ?? []).join(' | ') || '-',
    }))

    const professorRows: UnifiedPersonRow[] = professors.map((professor) => ({
      id: professor.id,
      name: professor.name ?? '',
      phoneNumber: professor.phoneNumber ?? '',
      emailAddress: professor.emailAddress ?? '',
      status: professor.status ?? '',
      personType: 'PROFESSOR',
      salary: professor.salary,
      addressText: (addressByPerson.get(professor.id) ?? []).join(' | ') || '-',
    }))

    return [...studentRows, ...professorRows].sort((a, b) => a.id - b.id)
  }, [addressByPerson, professors, students])

  const visibleRows = useMemo(() => {
    const query = search.trim().toLowerCase()
    if (!query) {
      return rows
    }

    return rows.filter((row) => {
      return (
        row.name.toLowerCase().includes(query) ||
        row.emailAddress.toLowerCase().includes(query)
      )
    })
  }, [rows, search])

  const selectablePeople = useMemo(() => {
    const rowTypeById = new Map<number, PersonType>(
      rows.map((row) => [row.id, row.personType])
    )

    return people
      .map((person) => {
        const personType = rowTypeById.get(person.id)
        const labelType = personType === 'STUDENT'
          ? 'Estudante'
          : personType === 'PROFESSOR'
            ? 'Professor'
            : 'Pessoa'

        return {
          id: person.id,
          label: `${person.id} - ${person.name} (${labelType})`,
        }
      })
      .sort((a, b) => a.id - b.id)
  }, [people, rows])

  const closeModal = () => {
    if (!submitting) {
      setModal({ kind: 'none' })
    }
  }

  const addressesBySelectedPerson = useMemo(() => {
    if (modal.kind === 'view-student' || modal.kind === 'view-professor') {
      return addresses.filter((address) => address.person?.id === modal.record.id)
    }
    return []
  }, [addresses, modal])

  const handleStudentCreate = async (payload: StudentInput) => {
    setSubmitting(true)
    setError(null)
    setFeedback(null)
    try {
      await api.createStudent(payload)
      setFeedback('Estudante cadastrado com sucesso.')
      setModal({ kind: 'none' })
      await loadData()
    } catch (submitError) {
      setError(
        submitError instanceof Error
          ? submitError.message
          : 'Não foi possível cadastrar o estudante.'
      )
    } finally {
      setSubmitting(false)
    }
  }

  const handleProfessorCreate = async (payload: ProfessorInput) => {
    setSubmitting(true)
    setError(null)
    setFeedback(null)
    try {
      await api.createProfessor(payload)
      setFeedback('Professor cadastrado com sucesso.')
      setModal({ kind: 'none' })
      await loadData()
    } catch (submitError) {
      setError(
        submitError instanceof Error
          ? submitError.message
          : 'Não foi possível cadastrar o professor.'
      )
    } finally {
      setSubmitting(false)
    }
  }

  const handleStudentUpdate = async (id: number, payload: StudentInput) => {
    setSubmitting(true)
    setError(null)
    setFeedback(null)
    try {
      await api.updateStudent(id, payload)
      setFeedback('Estudante atualizado com sucesso.')
      setModal({ kind: 'none' })
      await loadData()
    } catch (submitError) {
      setError(
        submitError instanceof Error
          ? submitError.message
          : 'Não foi possível atualizar o estudante.'
      )
    } finally {
      setSubmitting(false)
    }
  }

  const handleProfessorUpdate = async (
    id: number,
    payload: ProfessorInput
  ) => {
    setSubmitting(true)
    setError(null)
    setFeedback(null)
    try {
      await api.updateProfessor(id, payload)
      setFeedback('Professor atualizado com sucesso.')
      setModal({ kind: 'none' })
      await loadData()
    } catch (submitError) {
      setError(
        submitError instanceof Error
          ? submitError.message
          : 'Não foi possível atualizar o professor.'
      )
    } finally {
      setSubmitting(false)
    }
  }

  const handleAddressCreate = async (payload: AddressInput) => {
    setSubmitting(true)
    setError(null)
    setFeedback(null)
    try {
      await api.createAddress(payload)
      setFeedback('Endereço cadastrado com sucesso.')
      setModal({ kind: 'none' })
      await loadData()
    } catch (submitError) {
      setError(
        submitError instanceof Error
          ? submitError.message
          : 'Não foi possível cadastrar o endereço.'
      )
    } finally {
      setSubmitting(false)
    }
  }

  const handleAddressUpdate = async (id: number, payload: AddressInput) => {
    setSubmitting(true)
    setError(null)
    setFeedback(null)
    try {
      await api.updateAddress(id, payload)
      setFeedback('Endereço atualizado com sucesso.')
      setModal({ kind: 'none' })
      await loadData()
    } catch (submitError) {
      setError(
        submitError instanceof Error
          ? submitError.message
          : 'Não foi possível atualizar o endereço.'
      )
    } finally {
      setSubmitting(false)
    }
  }

  const handleAddressDelete = async (address: Address) => {
    const confirmed = window.confirm(
      `Confirma a exclusão do endereço #${address.id}?`
    )

    if (!confirmed) {
      return
    }

    setSubmitting(true)
    setError(null)
    setFeedback(null)

    try {
      await api.deleteAddress(address.id)
      setFeedback('Endereço excluído com sucesso.')
      await loadData()
    } catch (deleteError) {
      setError(
        deleteError instanceof Error
          ? deleteError.message
          : 'Não foi possível excluir o endereço.'
      )
    } finally {
      setSubmitting(false)
    }
  }

  const handleDelete = async (row: UnifiedPersonRow) => {
    const label = row.personType === 'STUDENT' ? 'estudante' : 'professor'
    const confirmed = window.confirm(
      `Confirma a exclusão de ${label} #${row.id}?`
    )

    if (!confirmed) {
      return
    }

    setSubmitting(true)
    setError(null)
    setFeedback(null)

    try {
      if (row.personType === 'STUDENT') {
        await api.deleteStudent(row.id)
      } else {
        await api.deleteProfessor(row.id)
      }
      setFeedback('Registro excluído com sucesso.')
      await loadData()
    } catch (deleteError) {
      setError(
        deleteError instanceof Error
          ? deleteError.message
          : 'Não foi possível excluir o registro.'
      )
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="app">
      <header className="header">
        <h1>BJ Management</h1>
        <p>Gerenciamento de pessoas</p>
      </header>

      <section className="actions">
        <button
          type="button"
          className="primary"
          onClick={() => setModal({ kind: 'create-student' })}
          disabled={submitting}
        >
          Adicionar estudante
        </button>
        <button
          type="button"
          className="primary"
          onClick={() => setModal({ kind: 'create-professor' })}
          disabled={submitting}
        >
          Adicionar professor
        </button>
        <button
          type="button"
          className="primary"
          onClick={() => setModal({ kind: 'create-address' })}
          disabled={submitting}
        >
          Adicionar endereço
        </button>
      </section>

      <section className="view-switch" aria-label="Alternar visualização">
        <button
          type="button"
          className={activeView === 'PEOPLE' ? 'primary' : 'secondary'}
          onClick={() => setActiveView('PEOPLE')}
          disabled={submitting}
        >
          Visualizar pessoas
        </button>
        <button
          type="button"
          className={activeView === 'ADDRESSES' ? 'primary' : 'secondary'}
          onClick={() => setActiveView('ADDRESSES')}
          disabled={submitting}
        >
          Visualizar endereços
        </button>
      </section>

      <section className="toolbar">
        {activeView === 'PEOPLE' && (
          <input
            value={search}
            onChange={(event) => setSearch(event.target.value)}
            placeholder="Buscar por nome ou e-mail"
            aria-label="Buscar por nome ou e-mail"
          />
        )}
      </section>

      {feedback && <p className="feedback success">{feedback}</p>}
      {error && <p className="feedback error">{error}</p>}

      {activeView === 'PEOPLE' && (
        <section className="table-wrapper">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>E-mail</th>
                <th>Telefone</th>
                <th>Tipo de pessoa</th>
                <th>Status</th>
                <th>Endereço</th>
                <th>Matrícula</th>
                <th>Salário</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {loading && (
                <tr>
                  <td colSpan={10} className="empty">
                    Carregando dados...
                  </td>
                </tr>
              )}

              {!loading && visibleRows.length === 0 && rows.length === 0 && (
                <tr>
                  <td colSpan={10} className="empty">
                    Nenhum estudante ou professor encontrado.
                  </td>
                </tr>
              )}

              {!loading && visibleRows.length === 0 && rows.length > 0 && (
                <tr>
                  <td colSpan={10} className="empty">
                    Nenhum resultado para a busca informada.
                  </td>
                </tr>
              )}

              {!loading &&
                visibleRows.map((row) => (
                  <tr key={`${row.personType}-${row.id}`}>
                    <td>{row.id}</td>
                    <td>{row.name || '-'}</td>
                    <td>{row.emailAddress || '-'}</td>
                    <td>{row.phoneNumber || '-'}</td>
                    <td>{row.personType === 'STUDENT' ? 'Estudante' : 'Professor'}</td>
                    <td>{row.status || '-'}</td>
                    <td>{row.addressText}</td>
                    <td>{row.studentNumber || '-'}</td>
                    <td>{row.salary ?? '-'}</td>
                    <td>
                      <div className="row-actions">
                        <button
                          type="button"
                          className="secondary"
                          onClick={() => {
                            if (row.personType === 'STUDENT') {
                              const record = students.find((item) => item.id === row.id)
                              if (record) {
                                setModal({ kind: 'view-student', record })
                              }
                            } else {
                              const record = professors.find((item) => item.id === row.id)
                              if (record) {
                                setModal({ kind: 'view-professor', record })
                              }
                            }
                          }}
                          disabled={submitting}
                        >
                          Visualizar
                        </button>
                        <button
                          type="button"
                          className="secondary"
                          onClick={() => {
                            if (row.personType === 'STUDENT') {
                              const record = students.find((item) => item.id === row.id)
                              if (record) {
                                setModal({ kind: 'edit-student', record })
                              }
                            } else {
                              const record = professors.find((item) => item.id === row.id)
                              if (record) {
                                setModal({ kind: 'edit-professor', record })
                              }
                            }
                          }}
                          disabled={submitting}
                        >
                          Editar
                        </button>
                        <button
                          type="button"
                          className="danger"
                          onClick={() => void handleDelete(row)}
                          disabled={submitting}
                        >
                          Excluir
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
            </tbody>
          </table>
        </section>
      )}

      {activeView === 'ADDRESSES' && (
        <section className="table-wrapper">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Pessoa</th>
                <th>Rua</th>
                <th>Cidade</th>
                <th>Estado</th>
                <th>CEP</th>
                <th>País</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {loading && (
                <tr>
                  <td colSpan={8} className="empty">
                    Carregando endereços...
                  </td>
                </tr>
              )}

              {!loading && addresses.length === 0 && (
                <tr>
                  <td colSpan={8} className="empty">
                    Nenhum endereço encontrado.
                  </td>
                </tr>
              )}

              {!loading &&
                addresses.map((address) => (
                  <tr key={address.id}>
                    <td>{address.id}</td>
                    <td>{address.person?.name ? `${address.person.id} - ${address.person.name}` : '-'}</td>
                    <td>{address.street || '-'}</td>
                    <td>{address.city || '-'}</td>
                    <td>{address.state || '-'}</td>
                    <td>{address.zipCode || '-'}</td>
                    <td>{address.country || '-'}</td>
                    <td>
                      <div className="row-actions">
                        <button
                          type="button"
                          className="secondary"
                          onClick={() => setModal({ kind: 'edit-address', record: address })}
                          disabled={submitting}
                        >
                          Editar
                        </button>
                        <button
                          type="button"
                          className="danger"
                          onClick={() => void handleAddressDelete(address)}
                          disabled={submitting}
                        >
                          Excluir
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
            </tbody>
          </table>
        </section>
      )}

      {modal.kind !== 'none' && (
        <Modal onClose={closeModal}>
          {modal.kind === 'create-student' && (
            <PersonForm
              type="STUDENT"
              title="Adicionar estudante"
              submitting={submitting}
              onCancel={closeModal}
              onSubmit={(values) => void handleStudentCreate(values)}
            />
          )}

          {modal.kind === 'create-professor' && (
            <PersonForm
              type="PROFESSOR"
              title="Adicionar professor"
              submitting={submitting}
              onCancel={closeModal}
              onSubmit={(values) => void handleProfessorCreate(values)}
            />
          )}

          {modal.kind === 'edit-student' && (
            <PersonForm
              type="STUDENT"
              title={`Editar estudante #${modal.record.id}`}
              initialData={modal.record}
              submitting={submitting}
              onCancel={closeModal}
              onSubmit={(values) =>
                void handleStudentUpdate(modal.record.id, values)
              }
            />
          )}

          {modal.kind === 'edit-professor' && (
            <PersonForm
              type="PROFESSOR"
              title={`Editar professor #${modal.record.id}`}
              initialData={modal.record}
              submitting={submitting}
              onCancel={closeModal}
              onSubmit={(values) =>
                void handleProfessorUpdate(modal.record.id, values)
              }
            />
          )}

          {modal.kind === 'view-student' && (
            <PersonDetails
              type="STUDENT"
              record={modal.record}
              addresses={addressesBySelectedPerson}
              onClose={closeModal}
            />
          )}

          {modal.kind === 'view-professor' && (
            <PersonDetails
              type="PROFESSOR"
              record={modal.record}
              addresses={addressesBySelectedPerson}
              onClose={closeModal}
            />
          )}

          {modal.kind === 'create-address' && (
            <AddressForm
              title="Adicionar endereço"
              people={selectablePeople}
              submitting={submitting}
              onCancel={closeModal}
              onSubmit={(values) => void handleAddressCreate(values)}
            />
          )}

          {modal.kind === 'edit-address' && (
            <AddressForm
              title={`Editar endereço #${modal.record.id}`}
              initialData={modal.record}
              submitLabel="Atualizar"
              people={selectablePeople}
              submitting={submitting}
              onCancel={closeModal}
              onSubmit={(values) =>
                void handleAddressUpdate(modal.record.id, values)
              }
            />
          )}
        </Modal>
      )}
    </div>
  )
}

export default App
