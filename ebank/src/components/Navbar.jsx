// import React from "react";
// import { Navbar as BsNavbar, Nav, Container, Button } from "react-bootstrap";
// import { NavLink, useNavigate } from "react-router-dom";
// import { useAuth} from "../auth/AuthContext";

// import "./NavBar.css"; // Import the custom Navbar CSS

// function Navbar() {
//   const { user, logout } = useAuth();
//   const navigate = useNavigate();

//   // If no user is logged in, we don't show the navbar.
//   if (!user) return null;

//   const handleLogout = () => {
//     logout();
//     navigate("/login"); // Redirect to login after logout
//   };

//   return (
//     <>
//       {/* --- Logo Banner --- */}
//       <div className="navbar-logo-banner">
//         <img
//           src="/images/logoM.jpg" // Logo placed in public/assets/img/logoM.jpg
//           alt="Logo MENPS"
//           className="img-fluid"
//         />
//       </div>

//       {/* --- Navbar --- */}
//       <BsNavbar
//         expand="lg"
//         variant="dark"
//         className="navbar shadow-sm sticky-top"
//       >
//         <Container fluid>
//           {/* Brand Logo & Home Link */}
//           <BsNavbar.Brand as={NavLink} to="/" className="navbar-brand">
//             <i className="bi bi-shield-lock me-1" />
//             Amanis
//           </BsNavbar.Brand>

//           {/* Burger Menu for Mobile */}
//           <BsNavbar.Toggle aria-controls="navbarNav" />

//           <BsNavbar.Collapse id="navbarNav">
//             <Nav className="ms-auto align-items-lg-center">

//               {/* User Info (Name & Role) */}
//               <Nav.Link className="text-white">
//                 <span className="navbar-text">
//                   <i className="bi bi-person-circle me-1" />
//                   {user.name}
//                   <span className="badge bg-light text-dark ms-2">User</span>
//                 </span>
//               </Nav.Link>

//               <Button
//                 variant="outline-light"
//                 size="sm"
//                 onClick={handleLogout}
//                 className="ms-lg-3 mt-2 mt-lg-0"
//               >
//                 <i className="bi bi-box-arrow-right me-1" />
//                 Déconnexion
//               </Button>
//             </Nav>
//           </BsNavbar.Collapse>
//         </Container>
//       </BsNavbar>
//     </>
//   );
// }

// export default Navbar;
