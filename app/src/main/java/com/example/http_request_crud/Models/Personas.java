package com.example.http_request_crud.Models;

public class Personas {
  private String nombres;
  private String apellido;
  private String direccion;
  private String telefono;
  private String foto;

  public Personas(String nombres, String apellido, String direccion, String telefono, String foto) {
    this.nombres = nombres;
    this.apellido = apellido;
    this.direccion = direccion;
    this.telefono = telefono;
    this.foto = foto;
  }

  public Personas() {

  }

  public String getNombres() {
    return nombres;
  }

  public void setNombres(String nombres) {
    this.nombres = nombres;
  }

  public String getApellido() {
    return apellido;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getFoto() {
    return foto;
  }

  public void setFoto(String foto) {
    this.foto = foto;
  }
}
