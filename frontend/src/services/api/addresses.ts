import type { Address } from '../../types'
import { deleteRequest, jsonRequest, request } from './client'
import type { AddressInput } from './types'

export const addressesApi = {
  list: () => request<Address[]>('/address'),
  create: (payload: AddressInput) => jsonRequest<Address>('/address', 'POST', payload),
  update: (id: number, payload: AddressInput) =>
    jsonRequest<Address>(`/address/${id}`, 'PUT', payload),
  delete: (id: number) => deleteRequest(`/address/${id}`),
}
