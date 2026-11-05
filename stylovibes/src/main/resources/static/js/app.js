// StyloVibes · utilidades de interfaz
document.addEventListener('DOMContentLoaded', () => {
  // Confirmación para acciones destructivas: <form data-confirm="mensaje">
  document.querySelectorAll('form[data-confirm]').forEach(f => {
    f.addEventListener('submit', e => {
      if (!window.confirm(f.dataset.confirm)) e.preventDefault();
    });
  });
});
