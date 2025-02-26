import {FormEvent, useEffect, useRef, useState} from 'react'

function App() {
    const [message, setMessage] = useState('')
    const [messages, setMessages] = useState([])
    const ws = useRef<WebSocket>(null)



    // 환경변수에서 WebSocket 서버 주소 가져오기
    const websocketUrl = import.meta.env.VITE_CHAT_SERVER || 'ws://localhost:8080';

    useEffect(() => {
        // WebSocket 연결
        ws.current = new WebSocket(websocketUrl)

        // 메시지 수신 시 처리
        ws.current.onmessage = ({ data }) => {
            setMessages((prevMessages) => [...prevMessages, data as never])
        }

        // 연결 종료 시 처리
        ws.current.onclose = () => {
            console.log('WebSocket disconnected')
        }

        // 컴포넌트 언마운트 시 WebSocket 연결 종료
        return () => {
            ws.current?.close()
        }
    }, [])

    const sendMessage = (e: FormEvent) => {
        e.preventDefault()
        if (message && ws.current?.readyState === WebSocket.OPEN) {
            ws.current?.send(message)
            setMessage('')
        }
    }

    return (
        <div className="flex flex-col h-screen bg-gray-100 p-4 gap-4">
            <form onSubmit={sendMessage} className="flex">
                <input
                    type="text"
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    className="flex-grow p-2 border rounded-l focus:bg-amber-200"
                    placeholder="Type a message..."
                />
                <button type="submit" className="bg-blue-500 text-white p-2 rounded-r">
                    Send
                </button>
            </form>
            <div className="flex-grow overflow-y-auto mb-4 flex flex-col gap-2">
                {messages.map((msg, index) => (
                    <div key={index} className="bg-white p-2 rounded shadow">
                        {msg}
                    </div>
                ))}
            </div>
        </div>
    )
}

export default App
