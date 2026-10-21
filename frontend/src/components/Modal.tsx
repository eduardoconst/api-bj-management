import type { PropsWithChildren } from 'react'

type ModalProps = PropsWithChildren<{
  onClose: () => void
}>

export function Modal({ children, onClose }: ModalProps) {
  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div
        className="modal-content"
        onClick={(event) => event.stopPropagation()}
      >
        {children}
      </div>
    </div>
  )
}
