import { useEffect, useState } from 'react'
import axios from 'axios'
import FoglalkozasUrlap from './FoglalkozasUrlap.jsx'
import './App.css'

function App() {
  // állapotok (state): ha változnak, a React újrarajzolja az oldalt
  const [foglalkozasok, setFoglalkozasok] = useState([])
  const [megyek, setMegyek] = useState([])
  const [betoltes, setBetoltes] = useState(true)
  const [hiba, setHiba] = useState(null)
  const [szerkesztett, setSzerkesztett] = useState(null) // az éppen módosított sor (null: nincs ilyen)
  const [torlesHiba, setTorlesHiba] = useState(null)

  // egyszer fut le
  useEffect(() => {
    Promise.all([axios.get('/api/foglalkozasok'), axios.get('/api/megyek')])
      .then(([foglalkozasValasz, megyeValasz]) => {
        setFoglalkozasok(foglalkozasValasz.data)
        setMegyek(megyeValasz.data)
      })
      .catch(() => setHiba('Nem sikerült betölteni az adatokat. Fut a Spring Boot alkalmazás?'))
      .finally(() => setBetoltes(false))
  }, [])

  // megyekód -> megyenév
  const megyeNev = (kod) => megyek.find((m) => m.megyekod === kod)?.megyenev ?? kod

  // szám tagolás
  const szam = (n) => n.toLocaleString('hu-HU')

  // Create: POST
  const ujFelvetele = (adat) =>
    axios.post('/api/foglalkozasok', adat)
      .then((valasz) => setFoglalkozasok([...foglalkozasok, valasz.data]))

  // Update: PUT
  const modositas = (adat) =>
    axios.put(`/api/foglalkozasok/${szerkesztett.fkod}`, adat)
      .then((valasz) => {
        setFoglalkozasok(foglalkozasok.map((f) => (f.fkod === valasz.data.fkod ? valasz.data : f)))
        setSzerkesztett(null)
      })

  // Delete
  const torles = (f) => {
    if (!confirm(`Biztosan törlöd ezt a sort? ${megyeNev(f.megyekod)} (${f.fkod})`)) return
    setTorlesHiba(null)
    axios.delete(`/api/foglalkozasok/${f.fkod}`)
      .then(() => {
        setFoglalkozasok(foglalkozasok.filter((x) => x.fkod !== f.fkod))
        if (szerkesztett?.fkod === f.fkod) setSzerkesztett(null)
      })
      .catch((err) => setTorlesHiba(err.response?.data?.message || 'Nem sikerült törölni.'))
  }

  if (betoltes) return <p>Betöltés…</p>
  if (hiba) return <p className="hiba">{hiba}</p>

  return (
    <div className="foglalkozasok">
      <h2>Foglalkoztatás megyénként</h2>
      {szerkesztett ? (
        <>
          <h3>Sor módosítása ({szerkesztett.fkod})</h3>
          {/* a key miatt másik sor kiválasztásakor új, újra kitöltött űrlap készül */}
          <FoglalkozasUrlap
            key={szerkesztett.fkod}
            megyek={megyek}
            gombFelirat="Mentés"
            kezdoAdat={szerkesztett}
            onMentes={modositas}
            onMegse={() => setSzerkesztett(null)}
          />
        </>
      ) : (
        <>
          <h3>Új sor felvétele</h3>
          <FoglalkozasUrlap megyek={megyek} gombFelirat="Hozzáad" onMentes={ujFelvetele} />
        </>
      )}
      <p>{foglalkozasok.length} sor</p>
      {torlesHiba && <p className="hiba">{torlesHiba}</p>}
      <table>
        <thead>
          <tr>
            <th>Kód</th>
            <th>Megye</th>
            <th className="szam">Mezőgazdaság</th>
            <th className="szam">Ipar</th>
            <th className="szam">Szolgáltatás</th>
            <th className="szam">Összesen</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {foglalkozasok.map((f) => (
            <tr key={f.fkod} className={szerkesztett?.fkod === f.fkod ? 'kijelolt' : ''}>
              <td>{f.fkod}</td>
              <td>{megyeNev(f.megyekod)}</td>
              <td className="szam">{szam(f.mezogazdasag)}</td>
              <td className="szam">{szam(f.ipar)}</td>
              <td className="szam">{szam(f.szolgaltatas)}</td>
              <td className="szam">{szam(f.mezogazdasag + f.ipar + f.szolgaltatas)}</td>
              <td className="muveletek">
                <button type="button" onClick={() => setSzerkesztett(f)}>Módosít</button>
                <button type="button" onClick={() => torles(f)}>Töröl</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

export default App
