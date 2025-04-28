import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; // Import useNavigate for programmatic navigation
import '../index.css'; // Global CSS for basic styles
import Background from '../Components/background'; // Import Background component
import axios from 'axios';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate(); // Initialize navigate

  const handleLogin = async () => {
    console.log("Saving new user:", username, password);

    try {
        const response = await axios.post(
            'http://localhost:8080/api/auth/login',
            { email: username, password },
            { headers: { 'Content-Type': 'application/json' } }
        );

        alert(response.data); // Notify the user that their input was saved
        navigate('/checkURL');
      } catch (error) {
        console.error('Error saving user:', error);
        alert('Error saving user, please try again.');
    }
};

  return (
    <div className="login-page min-h-screen flex justify-center items-center bg-[#080710] relative">
      <Background />

      <div className="login-page-inner bg-white bg-opacity-10 border-2 border-white border-opacity-10 shadow-xl rounded-xl backdrop-blur-lg p-8 w-96 z-10 relative">
        <header className="header text-center text-4xl font-semibold text-white mb-12">
          <h1>Login</h1>
        </header>

        <div className="mb-6">
          <label htmlFor="username" className="label text-white mb-1 block">Email:</label>
          <input
            id="username"
            type="text"
            className="input-field w-full bg-[#37373E] text-white h-12 rounded-sm p-3 text-sm"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="Enter email"
            required
          />
        </div>

        <div className="mb-6">
          <label htmlFor="password" className="label text-white mb-1 block">Password:</label>
          <input
            id="password"
            type="password"
            className="input-field w-full bg-[#37373E] text-white h-12 rounded-sm p-3 text-sm"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Enter password"
            required
          />
        </div>

        <button
          onClick={handleLogin}
          className="button w-full bg-white text-gray-800 py-3 text-lg font-semibold rounded-md hover:bg-blue-700 hover:text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          Log In
        </button>

        <footer className="footer text-white text-center mt-8">
          <p>Commerce Bank  © 2025</p>
        </footer>
      </div>
    </div>
  );
};

export default Login;