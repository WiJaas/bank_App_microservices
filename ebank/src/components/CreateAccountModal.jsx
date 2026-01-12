import { useState } from "react";
import { employeeCreateAccount } from "../services/employeeService";

export default function CreateAccountModal({ clients = [], onClose, onCreated }) {

    const [form, setForm] = useState({
        cin: "",
        type: "CURRENT",
        initialBalance: 0
    });

    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState("");

    const handleChange = (e) => {
        const { name, value } = e.target;

        // convert balance to number
        if (name === "initialBalance") {
            setForm({ ...form, [name]: Number(value) });
        } else {
            setForm({ ...form, [name]: value });
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSubmitting(true);
        setError("");

        try {
            await employeeCreateAccount(form);
            onCreated?.();
            onClose?.();
        } catch (err) {
            console.error(err);
            setError("Erreur lors de la création du compte");
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div className="fixed inset-0 bg-black/30 flex items-center justify-center">
            <div className="bg-white rounded-2xl p-6 w-[420px] shadow">

                <h2 className="text-lg font-semibold mb-4">
                    Créer un compte bancaire
                </h2>

                <form onSubmit={handleSubmit} className="space-y-4">

                    {/* CLIENT CIN */}
                    <div>
                        <label className="text-sm">Client</label>
                        <select
                            required
                            name="cin"
                            value={form.cin}
                            onChange={handleChange}
                            className="w-full border rounded-lg p-2"
                        >
                            <option value="">Sélectionner…</option>

                            {clients?.map(c => (
                                <option key={c.id} value={c.cin}>
                                    {c.firstName} {c.lastName} — {c.cin}
                                </option>
                            ))}
                        </select>
                    </div>

                    {/* ACCOUNT TYPE */}
                    <div>
                        <label className="text-sm">Type de compte</label>
                        <select
                            name="type"
                            value={form.type}
                            onChange={handleChange}
                            className="w-full border rounded-lg p-2"
                        >
                            <option value="CURRENT">Courant</option>
                            <option value="SAVINGS">Épargne</option>
                        </select>
                    </div>

                    {/* INITIAL BALANCE */}
                    <div>
                        <label className="text-sm">Solde initial</label>
                        <input
                            name="initialBalance"
                            type="number"
                            step="0.01"
                            value={form.initialBalance}
                            onChange={handleChange}
                            className="w-full border rounded-lg p-2"
                        />
                    </div>

                    {error && (
                        <div className="text-red-600 text-sm">{error}</div>
                    )}

                    <div className="flex justify-end gap-3 pt-2">
                        <button
                            type="button"
                            className="px-4 py-2 rounded-lg border"
                            onClick={onClose}
                        >
                            Annuler
                        </button>

                        <button
                            disabled={submitting}
                            className="px-4 py-2 rounded-lg bg-green-600 text-white"
                        >
                            {submitting ? "Création…" : "Créer"}
                        </button>
                    </div>

                </form>
            </div>
        </div>
    );
}
