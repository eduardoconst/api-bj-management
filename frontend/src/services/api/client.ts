type ApiErrorResponse = {
  message?: string
}

const API_BASE = '/api'

const jsonHeaders = {
  'Content-Type': 'application/json',
}

type RequestOptions = RequestInit & { skipJson?: boolean }

export async function request<T>(
  path: string,
  init?: RequestOptions
): Promise<T> {
  const response = await fetch(`${API_BASE}${path}`, init)

  if (!response.ok) {
    let errorMessage = `Erro HTTP ${response.status}`

    try {
      const body = (await response.json()) as ApiErrorResponse
      if (body.message) {
        errorMessage = body.message
      }
    } catch {
      const text = await response.text()
      if (text) {
        errorMessage = text
      }
    }

    throw new Error(errorMessage)
  }

  if (response.status === 204 || init?.skipJson) {
    return undefined as T
  }

  return (await response.json()) as T
}

export function jsonRequest<T>(
  path: string,
  method: 'POST' | 'PUT' | 'PATCH',
  payload: unknown
) {
  return request<T>(path, {
    method,
    headers: jsonHeaders,
    body: JSON.stringify(payload),
  })
}

export function deleteRequest(path: string) {
  return request<void>(path, { method: 'DELETE', skipJson: true })
}
