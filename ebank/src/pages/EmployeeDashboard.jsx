import { useEffect, useState } from "react";
import { employeeListClients,employeeGetClientAccounts} from "../services/employeeService";
import NavbarEmployee from "../components/NavbarEmployee";
import CreateClientModal from "../components/CreateClientModal";
import CreateAccountModal from "../components/CreateAccountModal";
import { Fragment } from "react";


export default function EmployeeDashboard() {

  const [clients, setClients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [expandedClientId, setExpandedClientId] = useState(null);
  const [accountsByClient, setAccountsByClient] = useState({});

  const [openCreateModal, setOpenCreateModal] = useState(false);
  const [openCreateAccountModal, setOpenCreateAccountModal] = useState(false);

  useEffect(() => {
    loadClients();
  }, []);

  const loadClients = async () => {
    try {
      setLoading(true);

      const data = await employeeListClients();

      const list =
          Array.isArray(data)
              ? data
              : Array.isArray(data?.content)
                  ? data.content
                  : [];

      setClients(list);
    } catch (e) {
      console.error(e);
      setClients([]);
    } finally {
      setLoading(false);
    }
  };

  const toggleAccounts = async (clientId) => {
    if (expandedClientId === clientId) {
      setExpandedClientId(null);
      return;
    }

    if (!accountsByClient[clientId]) {
      const data = await employeeGetClientAccounts(clientId);

      setAccountsByClient(prev => ({
        ...prev,
        [clientId]: data
      }));
    }

    setExpandedClientId(clientId);
  };

  function AccountsList({ accounts }) {
    if (!accounts) {
      return <p className="text-slate-400">Chargement des comptes…</p>;
    }

    if (accounts.length === 0) {
      return <p className="text-slate-400">Aucun compte pour ce client.</p>;
    }

    return (
        <table className="w-full text-sm border rounded-lg">
          <thead className="bg-slate-100">
          <tr>
            <th className="px-4 py-2 text-left">RIB</th>
            <th className="px-4 py-2 text-left">Type</th>
            <th className="px-4 py-2 text-left">Solde</th>
            <th className="px-4 py-2 text-left">Statut</th>
          </tr>
          </thead>
          <tbody>
          {accounts.map(acc => (
              <tr key={acc.rib} className="border-t">
                <td className="px-4 py-2">{acc.rib}</td>
                <td className="px-4 py-2">{acc.type}</td>
                <td className="px-4 py-2">{acc.balance} MAD</td>
                <td className="px-4 py-2">{acc.status}</td>
              </tr>
          ))}
          </tbody>
        </table>
    );
  }


  return (
      <>
        <NavbarEmployee />

        <div className="max-w-7xl mx-auto px-6 py-10">

          <div className="flex items-center justify-between mb-8">
            <h1 className="text-2xl font-semibold">Gestion des clients</h1>

            <div className="flex gap-3">

              <button
                  onClick={() => setOpenCreateModal(true)}
                  className="px-5 py-2 rounded-xl bg-blue-600 text-white"
              >
                + Ajouter un client
              </button>

              <button
                  onClick={() => setOpenCreateAccountModal(true)}
                  className="px-5 py-2 rounded-xl bg-green-600 text-white"
              >
                + Créer un compte
              </button>

            </div>
          </div>

          <div className="bg-white rounded-2xl shadow ring-1 ring-slate-200 overflow-hidden">
            {loading ? (
                <div className="p-12 text-center text-slate-500">
                  Chargement…
                </div>
            ) : (
                <table className="min-w-full text-sm">
                  <thead className="bg-slate-50">
                  <tr>
                    <th className="px-6 py-3 text-left">CIN</th>
                    <th className="px-6 py-3 text-left">Prénom</th>
                    <th className="px-6 py-3 text-left">Nom</th>
                    <th className="px-6 py-3 text-left">Email</th>
                  </tr>
                  </thead>

                  <tbody>
                  {clients.map(client => (
                      <Fragment key={client.id}>
                        <tr className="border-t">
                          <td className="px-6 py-3">{client.cin}</td>
                          <td className="px-6 py-3">{client.firstName}</td>
                          <td className="px-6 py-3">{client.lastName}</td>
                          <td className="px-6 py-3">{client.email}</td>
                          <td className="px-6 py-3 text-right">
                            <button
                                className="text-blue-600"
                                onClick={() => toggleAccounts(client.id)}
                            >
                              {expandedClientId === client.id ? "Masquer comptes" : "Voir comptes"}
                            </button>
                          </td>
                        </tr>

                        {expandedClientId === client.id && (
                            <tr className="bg-slate-50">
                              <td colSpan={5} className="px-6 py-4">
                                <table className="w-full text-sm">
                                  <thead>
                                  <tr>
                                    <th>RIB</th>
                                    <th>Type</th>
                                    <th>Solde</th>
                                    <th>Statut</th>
                                  </tr>
                                  </thead>
                                  <tbody>
                                  {accountsByClient[client.id]?.map(account => (
                                      <tr key={account.id}>
                                        <td>{account.rib}</td>
                                        <td>{account.type}</td>
                                        <td>{account.balance} MAD</td>
                                        <td>{account.status}</td>
                                      </tr>
                                  ))}
                                  </tbody>
                                </table>
                              </td>
                            </tr>
                        )}
                      </Fragment>
                  ))}
                  {clients.length === 0 && (
                      <tr>
                        <td colSpan="4" className="px-6 py-12 text-center text-slate-400">
                          Aucun client
                        </td>
                      </tr>
                  )}
                  </tbody>
                </table>
            )}
          </div>
        </div>

        {openCreateModal && (
            <CreateClientModal
                onClose={() => setOpenCreateModal(false)}
                onCreated={loadClients}
            />
        )}

        {openCreateAccountModal && (
            <CreateAccountModal
                clients={clients}           // <<< IMPORTANT
                onClose={() => setOpenCreateAccountModal(false)}
                onCreated={loadClients}
            />
        )}
      </>
  );
}
