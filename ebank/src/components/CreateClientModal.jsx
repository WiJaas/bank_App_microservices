import { useState } from "react";
import { employeeCreateClient } from "../services/employeeService";

export default function CreateClientModal({ onClose, onCreated }) {

  const [form, setForm] = useState({
    cin: "",
    firstName: "",
    lastName: "",
    email: "",
    address: "",
    birthDate: "",
  });

  const [saving, setSaving] = useState(false);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);

    try {
      await employeeCreateClient({
        cin: form.cin,
        firstName: form.firstName,
        lastName: form.lastName,
        email: form.email,
        address: form.address,
        birthDate: form.birthDate,
      });

      onCreated?.();
      onClose();
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
      <div className="bg-white p-6 rounded-xl w-full max-w-lg">

        <h2 className="text-xl font-semibold mb-3">Nouveau client</h2>

        <form onSubmit={handleSubmit} className="space-y-3">

          <input
            name="cin"
            className="input"
            placeholder="CIN"
            onChange={handleChange}
            required
          />

          <div className="grid grid-cols-2 gap-3">
            <input
              name="firstName"
              placeholder="Prénom"
              className="input"
              onChange={handleChange}
              required
            />

            <input
              name="lastName"
              placeholder="Nom"
              className="input"
              onChange={handleChange}
              required
            />
          </div>

          <input
            name="email"
            type="email"
            className="input"
            placeholder="Email"
            onChange={handleChange}
            required
          />

          <input
            name="address"
            className="input"
            placeholder="Adresse"
            onChange={handleChange}
            required
          />

          <input
            name="birthDate"
            type="date"
            className="input"
            onChange={handleChange}
            required
          />

          <div className="flex justify-end gap-2 pt-2">
            <button
              type="button"
              onClick={onClose}
              className="btn-secondary"
            >
              Annuler
            </button>

            <button
              type="submit"
              disabled={saving}
              className="btn-primary"
            >
              {saving ? "Création…" : "Créer"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
