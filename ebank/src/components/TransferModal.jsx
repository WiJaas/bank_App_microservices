import { useState } from "react";
import { employeeCreateTransaction } from "../services/employeeService";

export default function TransferModal({ accounts, selectedAccountId, onClose, onSuccess }) {

    // 🔑 Hooks MUST be first (no conditions before them)
    const [destinationRib, setDestinationRib] = useState("");
    const [amount, setAmount] = useState("");
    const [reason, setReason] = useState("");
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const selectedAccount = accounts.find(a => a.id === selectedAccountId);

    if (!selectedAccount) {
        return null;
    }

    const handleSubmit = async () => {
        setError(null);

        // 1️⃣ Basic validations
        if (!destinationRib || !amount || !reason) {
            setError("Veuillez remplir tous les champs");
            return;
        }

        if (Number(amount) <= 0) {
            setError("Montant invalide");
            return;
        }

        // 2️⃣ Find destination account using RIB
        const destinationAccount = accounts.find(
            acc => String(acc.rib) === String(destinationRib)
        );

        if (!destinationAccount) {
            setError("Compte destinataire introuvable");
            return;
        }

        if (destinationAccount.id === selectedAccount.id) {
            setError("Le compte destinataire doit être différent");
            return;
        }

        try {
            setLoading(true);

            // ✅ THIS is where the code goes
            await employeeCreateTransaction({
                sourceAccountId: selectedAccount.id,
                destinationAccountId: destinationAccount.id,
                amount: Number(amount),
                description: reason
            });

            onSuccess();
            onClose();
        } catch (e) {
            setError("Erreur lors du virement");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
            <div className="bg-white rounded-xl w-full max-w-md p-6">
                <h3 className="text-lg font-semibold mb-4">Nouveau virement</h3>

                {/* Source RIB (read-only) */}
                <label className="block mb-2">Compte source</label>
                <input
                    value={selectedAccount.rib}
                    disabled
                    className="w-full border rounded px-3 py-2 mb-3 bg-slate-100"
                />

                <label className="block mb-2">Montant</label>
                <input
                    type="number"
                    value={amount}
                    onChange={e => setAmount(e.target.value)}
                    className="w-full border rounded px-3 py-2 mb-3"
                />

                <label className="block mb-2">RIB destinataire</label>
                <input
                    value={destinationRib}
                    onChange={e => setDestinationRib(e.target.value)}
                    className="w-full border rounded px-3 py-2 mb-3"
                />

                <label className="block mb-2">Motif</label>
                <input
                    value={reason}
                    onChange={e => setReason(e.target.value)}
                    className="w-full border rounded px-3 py-2 mb-3"
                />

                {error && <p className="text-red-600 text-sm mb-3">{error}</p>}

                <div className="flex justify-end gap-3">
                    <button onClick={onClose} className="px-4 py-2 border rounded">
                        Annuler
                    </button>
                    <button
                        type="button"
                        onClick={() => {
                            console.log("CLICKED");
                            handleSubmit();
                        }}
                        disabled={loading}
                        className="px-4 py-2 bg-green-600 text-white rounded"
                    >
                        Valider
                    </button>

                </div>
            </div>
        </div>
    );
}
