import { useEffect, useState } from "react";
import { getMyClientProfile } from "../services/clientService";
import {
  employeeGetClientAccounts,
  employeeGetAccountTransactions
} from "../services/employeeService";
import TransferModal from "../components/TransferModal";



export default function Dashboard() {

  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [accounts, setAccounts] = useState([]);
  const [selectedAccountId, setSelectedAccountId] = useState(null);
  const [transactions, setTransactions] = useState([]);
  const [page, setPage] = useState(0);
  const [openTransferModal, setOpenTransferModal] = useState(false);


  useEffect(() => {
    loadProfile();
  }, []);

  useEffect(() => {
    if (!profile?.id) return;

    employeeGetClientAccounts(profile.id).then(data => {
      setAccounts(data);

      if (data.length > 0) {
        setSelectedAccountId(data[0].id);
      }
    });
  }, [profile]);

  useEffect(() => {
    if (!selectedAccountId) return;

    employeeGetAccountTransactions(selectedAccountId)
        .then(data => setTransactions(data));

  }, [selectedAccountId, page]);


  const loadProfile = async () => {
    try {
      const data = await getMyClientProfile();
      setProfile(data);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
        <div className="min-h-screen flex items-center justify-center">
          Chargement…
        </div>
    );
  }

  return (
      <div className="min-h-screen bg-slate-50">

        <div className="bg-white border-b shadow-sm">
          <div className="max-w-6xl mx-auto px-6 py-4">
            <h1 className="text-xl font-semibold">
              Wijdane Bank — Espace Client
            </h1>
          </div>
        </div>

        <div className="max-w-6xl mx-auto px-6 py-10">

          <h2 className="text-2xl font-bold mb-4">
            Bonjour {profile?.firstName}
          </h2>

          <div className="grid gap-6 md:grid-cols-2">

            <div className="p-6 bg-white rounded-2xl shadow border">
              <h3 className="text-lg font-semibold mb-2">
                Informations personnelles
              </h3>

              <ul className="space-y-1">
                <li><b>CIN :</b> {profile?.cin}</li>
                <li><b>Nom :</b> {profile?.lastName}</li>
                <li><b>Prénom :</b> {profile?.firstName}</li>
                <li><b>Email :</b> {profile?.email}</li>
                <li><b>Adresse :</b> {profile?.address}</li>
                <li><b>Date de naissance :</b> {profile?.birthDate}</li>
              </ul>
            </div>

            <div className="p-6 bg-white rounded-2xl shadow border">
              <h3 className="text-lg font-semibold mb-2">
                Comptes bancaires
              </h3>
              {accounts.length === 0 ? (
                  <p className="text-slate-600">
                    Aucun compte bancaire.
                  </p>
              ) : (
                  <>
                    {/* Account selector */}
                    {accounts.length > 1 && (
                        <select
                            value={selectedAccountId}
                            onChange={(e) => {
                              setSelectedAccountId(Number(e.target.value));
                              setPage(0);
                            }}
                            className="mb-4 w-full border rounded px-3 py-2"
                        >
                          {accounts.map(acc => (
                              <option key={acc.id} value={acc.id}>
                                {acc.rib}
                              </option>
                          ))}
                        </select>
                    )}

                    {/* Account info */}
                    {accounts
                        .filter(acc => acc.id === selectedAccountId)
                        .map(acc => (
                            <div key={acc.id} className="mb-4">
                              <p><b>RIB :</b> {acc.rib}</p>
                              <p><b>Solde :</b> {acc.balance} MAD</p>
                            </div>
                        ))}

                    {/* Transactions */}
                    <h4 className="font-semibold mb-2">Dernières opérations</h4>

                    <table className="w-full text-sm border">
                      <thead className="bg-slate-100">
                      <tr>
                        <th className="p-2 text-left">Type</th>
                        <th className="p-2 text-left">Montant</th>
                        <th className="p-2 text-left">Date</th>
                        <th className="p-2 text-left">Intitulé</th>
                      </tr>
                      </thead>
                      <tbody>
                      {transactions.map(tx => (
                          <tr key={tx.id} className="border-t">
                            <td className="p-2">{tx.type}</td>
                            <td className="p-2">{tx.amount} MAD</td>
                            <td className="p-2">
                              {new Date(tx.transactionDate).toLocaleDateString()}
                            </td>
                            <td className="p-2">
                              {tx.description ?? "—"}
                            </td>
                          </tr>
                      ))}

                      {transactions.length === 0 && (
                          <tr>
                            <td colSpan="4" className="p-4 text-center text-slate-500">
                              Aucune opération
                            </td>
                          </tr>
                      )}
                      </tbody>

                    </table>

                    {/* Pagination */}
                    <div className="flex justify-between mt-3">
                      <button
                          onClick={() => setPage(p => Math.max(p - 1, 0))}
                          className="px-3 py-1 border rounded"
                      >
                        ◀
                      </button>

                      <button
                          onClick={() => setPage(p => p + 1)}
                          className="px-3 py-1 border rounded"
                      >
                        ▶
                      </button>
                    </div>

                    {/* New transfer */}
                    <button
                        onClick={() => setOpenTransferModal(true)}
                        className="mt-4 w-full bg-green-600 text-white py-2 rounded"
                    >
                      Nouveau virement
                    </button>
                    {openTransferModal && (
                        <TransferModal
                            accounts={accounts}
                            selectedAccountId={selectedAccountId}
                            onClose={() => setOpenTransferModal(false)}
                            onSuccess={async () => {
                              setPage(0);

                              const updatedAccounts = await employeeGetClientAccounts(profile.id);
                              setAccounts(updatedAccounts);

                              employeeGetAccountTransactions(selectedAccountId, 0)
                                  .then(res => setTransactions(res.content || []));
                            }}

                        />
                    )}


                  </>

              )}


            </div>

          </div>

        </div>
      </div>
  );
}
