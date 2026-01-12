// import { useState } from "react";
// import { useNavigate } from "react-router-dom";
// import { employeeCreateClient } from "../services/employeeService";
//
// export default function AddClient() {
//
//     const navigate = useNavigate();
//
//     const [form, setForm] = useState({
//         cin: "",
//         nom: "",
//         prenom: "",
//         dateNaissance: "",
//         email: "",
//         adressePostale: ""
//     });
//
//     const handleChange = (e) => {
//         setForm({ ...form, [e.target.name]: e.target.value });
//     };
//
//     const handleSubmit = async (e) => {
//         e.preventDefault();
//
//         await employeeCreateClient(form);
//
//         navigate("/employees");
//     };
//
//     return (
//         <div className="max-w-xl mx-auto mt-10">
//
//             <h2 className="text-xl font-semibold mb-4">Nouveau client</h2>
//
//             <form onSubmit={handleSubmit} className="space-y-3">
//
//                 <input name="cin" required className="input" placeholder="CIN" onChange={handleChange} />
//
//                 <div className="grid grid-cols-2 gap-3">
//                     <input name="nom" required className="input" placeholder="Nom" onChange={handleChange} />
//                     <input name="prenom" required className="input" placeholder="Prénom" onChange={handleChange} />
//                 </div>
//
//                 <input
//                     type="date"
//                     name="dateNaissance"
//                     required
//                     className="input"
//                     onChange={handleChange}
//                 />
//
//                 <input
//                     type="email"
//                     name="email"
//                     required
//                     className="input"
//                     placeholder="Email"
//                     onChange={handleChange}
//                 />
//
//                 <input
//                     name="adressePostale"
//                     className="input"
//                     placeholder="Adresse Postale"
//                     onChange={handleChange}
//                 />
//
//                 <button className="px-4 py-2 bg-blue-600 text-white rounded" type="submit">
//                     Créer
//                 </button>
//             </form>
//         </div>
//     );
// }
