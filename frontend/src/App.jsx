import { useEffect, useState } from 'react'
import axios from 'axios'
import './App.css'

function App() {
  // állapotok (state): ha változnak, a React újrarajzolja az oldalt
  const [foglalkozasok, setFoglalkozasok] = useState([])
  const [megyek, setMegyek] = useState([])
  const [betoltes, setBetoltes] = useState(true)
  const [hiba, setHiba] = useState(null)

  // egyszer fut le, amikor az oldal megjelenik: lekéri az adatokat a Spring Boot REST címeiről
  useEffect(() => {
    Promise.all([axios.get('/api/foglalkozasok'), axios.get('/api/megyek')])
      .then(([foglalkozasValasz, megyeValasz]) => {
        setFoglalkozasok(foglalkozasValasz.data)
        setMegyek(megyeValasz.data)
      })
      .catch(() => setHiba('Nem sikerült betölteni az adatokat. Fut a Spring Boot alkalmazás?'))
      .finally(() => setBetoltes(false))
  }, [])

  // megyekód -> megyenév (pl. 2 -> "Bács-Kiskun")
  const megyeNev = (kod) => megyek.find((m) => m.megyekod === kod)?.megyenev ?? kod

  // számok ezres tagolással: 142912 -> "142 912"
  const szam = (n) => n.toLocaleString('hu-HU')

  if (betoltes) return <p>Betöltés…</p>
  if (hiba) return <p className="hiba">{hiba}</p>

  return (
    <div className="foglalkozasok">
      <h2>Foglalkoztatás megyénként</h2>
      <p>{foglalkozasok.length} sor</p>
      <table>
        <thead>
          <tr>
            <th>Kód</th>
            <th>Megye</th>
            <th className="szam">Mezőgazdaság</th>
            <th className="szam">Ipar</th>
            <th className="szam">Szolgáltatás</th>
            <th className="szam">Összesen</th>
          </tr>
        </thead>
        <tbody>
          {foglalkozasok.map((f) => (
            <tr key={f.fkod}>
              <td>{f.fkod}</td>
              <td>{megyeNev(f.megyekod)}</td>
              <td className="szam">{szam(f.mezogazdasag)}</td>
              <td className="szam">{szam(f.ipar)}</td>
              <td className="szam">{szam(f.szolgaltatas)}</td>
              <td className="szam">{szam(f.mezogazdasag + f.ipar + f.szolgaltatas)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

export default App
