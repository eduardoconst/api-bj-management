import type { Address, Professor, Student } from '../types'

type PersonDetailsProps =
  | {
      type: 'STUDENT'
      record: Student
      addresses: Address[]
      onClose: () => void
    }
  | {
      type: 'PROFESSOR'
      record: Professor
      addresses: Address[]
      onClose: () => void
    }

export function PersonDetails(props: PersonDetailsProps) {
  return (
    <div className="details">
      <h2>{props.type === 'STUDENT' ? 'Detalhes do estudante' : 'Detalhes do professor'}</h2>

      <div className="details-grid">
        <div><strong>ID:</strong> {props.record.id}</div>
        <div><strong>Nome:</strong> {props.record.name || '-'}</div>
        <div><strong>E-mail:</strong> {props.record.emailAddress || '-'}</div>
        <div><strong>Telefone:</strong> {props.record.phoneNumber || '-'}</div>
        <div><strong>Status:</strong> {props.record.status || '-'}</div>
        <div><strong>Tipo:</strong> {props.type === 'STUDENT' ? 'Estudante' : 'Professor'}</div>
        {props.type === 'STUDENT' ? (
          <>
            <div><strong>Matrícula:</strong> {props.record.studentNumber || '-'}</div>
            <div><strong>Foto:</strong> {props.record.photo || '-'}</div>
          </>
        ) : (
          <div><strong>Salário:</strong> {props.record.salary ?? '-'}</div>
        )}
      </div>

      <div className="details-addresses">
        <h3>Endereços vinculados</h3>
        {props.addresses.length === 0 ? (
          <p>Nenhum endereço vinculado.</p>
        ) : (
          <ul>
            {props.addresses.map((address) => (
              <li key={address.id}>
                #{address.id} - {address.street}, {address.city}/{address.state}, CEP {address.zipCode}, {address.country}
              </li>
            ))}
          </ul>
        )}
      </div>

      <div className="form-actions">
        <button type="button" className="secondary" onClick={props.onClose}>
          Fechar
        </button>
      </div>
    </div>
  )
}
