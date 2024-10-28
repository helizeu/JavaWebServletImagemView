package javaBeans;

import com.mysql.cj.jdbc.Blob;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "CadView", urlPatterns = {"/CadView"})
@MultipartConfig
public class CadView extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=UTF-8");

            Usuario user = new Usuario();
            String sql = "select * from usuarios order by nome ";
            user.ps = user.con.prepareStatement(sql);
            user.tab = user.ps.executeQuery();
            String sHTML;
            try (PrintWriter out = response.getWriter()) {
                sHTML = "<!DOCTYPE html>"
                        + "<html lang=\"pt-br\">"
                        + "<head>"
                        + "    <meta charset=\"UTF-8\">"
                        + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                        + "    <title>Relação de contatos cadastrados no banco de dados</title>"
                        + "    <style>"
                        + "        table,tr,td {"
                        + "            border: 1px solid black;"
                        + "            border-collapse: collapse;"
                        + "            align-self: center;"
                        + "        }"
                        + "    </style>"
                        + "</head>"
                        + "<body  style=\"background-color: greenyellow;\">"
                        + "    <div align =center> <br> <br> <br>"
                        + "    <table style=\"background-color:antiquewhite;width: 40%;border: 1;border-style: solid; font-family: 'Times New Roman', Times, serif;border-color: antiquewhite;\">"
                        + "         <tr>"
                        + "               <th colspan=\"2\">*** Lista de usuários cadastrados no banco *** </th>"
                        + "         </tr>";
                out.print(sHTML);
                while (user.tab.next()) {
                    String nome = user.tab.getString("nome");
                    String email = user.tab.getString("email");
                    String celular = user.tab.getString("celular");
                    String nivel = user.tab.getString("nivel");

                    /* Código para Trazer a imagem do Banco para o HTML */
                    Blob blob = (Blob) user.tab.getBlob("foto");
                    if (blob == null) {
                        user.imagemBase64 = null;
                    } else {
                        user.foto = blob.getBinaryStream();
                        byte[] buffer = new byte[(int) blob.length()];
                        user.foto.read(buffer);
                        user.imagemBase64 = Base64.getEncoder().encodeToString(buffer);
                    }
                    /* Código para Trazer a imagem do Banco para o HTML */

                    String linha = "<tr><td>&nbsp;</td><td align =center> "
                            + "<input type = button value = \" Mensagem \">"
                            + "&nbsp;<input type = button value = \"Video Chamada\">"
                            + "</td></tr>";
                    linha += "<tr><td rowspan=4 style=\"width:10%;text-align: center;\"> ";
                    linha += "<img src=\"data:image/jpeg;base64," + user.imagemBase64 + "\" ";
                    linha += "style=\"height: 100px;width: 100px;\" >";
                    linha += "</td>";

                    linha += "<td style='width: 25%;'> " + nome + " </td>";
                    linha += "</tr><tr><td>" + email + "</td>";
                    linha += "</tr><tr><td>" + celular + "</td>";
                    linha += "</tr><tr><td>" + nivel + "</td></tr>";
                    out.print(linha);
                }
                sHTML = "</table> <br>"
                        + "<center> <a href=\"" + request.getContextPath() + "/javaJSP/sistema.jsp\"> Retornar </a></center></div></body></html>";
                out.print(sHTML);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CadView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
