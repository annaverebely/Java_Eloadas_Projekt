import { useState } from 'react'

const URES = { megyekod: '', mezogazdasag: '', ipar: '', szolgaltatas: '' }

// Űrlap felvételhez és módosításhoz

function FoglalkozasUrlap({ megyek, gombFelirat, onMentes, kezdoAdat, onMegse }) {
  // kezdőérték: módosításnál a kiválasztott sor adatai, felvételnél üres mezők
  const [adat, setAdat] = useState(kezdoAdat
    ? {
        megyekod: kezdoAdat.megyekod,
        mezogazdasag: kezdoAdat.mezogazdasag,
        ipar: kezdoAdat.ipar,
        szolgaltatas: kezdoAdat.szolgaltatas,
      }
    : URES)
  const [hiba, setHiba] = useState(null)


  const valtozas = (e) => setAdat({ ...adat, [e.target.name]: e.target.value })

  const kuldes = (e) => {
    e.preventDefault() // ne töltse újra az oldalt, mint egy hagyományos űrlap
    setHiba(null)
    onMentes({
      megyekod: Number(adat.megyekod),
      mezogazdasag: Number(adat.mezogazdasag),
      ipar: Number(adat.ipar),
      szolgaltatas: Number(adat.szolgaltatas),
    })
      .then(() => setAdat(URES))
      .catch((err) => setHiba(err.response?.data?.message || 'Nem sikerült menteni.'))
  }

  return (
    <form className="urlap" onSubmit={kuldes}>
      <label>
        Megye
        <select name="megyekod" value={adat.megyekod} onChange={valtozas} required>
          <option value="">– válassz –</option>
          {megyek.map((m) => (
            <option key={m.megyekod} value={m.megyekod}>{m.megyenev}</option>
          ))}
        </select>
      </label>
      <label>
        Mezőgazdaság
        <input type="number" name="mezogazdasag" min="0" value={adat.mezogazdasag} onChange={valtozas} required />
      </label>
      <label>
        Ipar
        <input type="number" name="ipar" min="0" value={adat.ipar} onChange={valtozas} required />
      </label>
      <label>
        Szolgáltatás
        <input type="number" name="szolgaltatas" min="0" value={adat.szolgaltatas} onChange={valtozas} required />
      </label>
      <button type="submit">{gombFelirat}</button>
      {onMegse && <button type="button" onClick={onMegse}>Mégse</button>}
      {hiba && <p className="hiba">{hiba}</p>}
    </form>
  )
}

export default FoglalkozasUrlap
