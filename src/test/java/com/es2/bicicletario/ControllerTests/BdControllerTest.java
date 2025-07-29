package com.es2.bicicletario.ControllerTests;

// @WebMvcTest(BdController.class)
// class BdControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private BdService bdService;

//     @Test
//     void quandoRestaurarBanco_eServicoFunciona_entaoRetornaStatus200Ok() throws Exception {
//         // Arrange (Preparação)
//         // Diz ao mock do serviço para não fazer nada e retornar com sucesso quando o método for chamado.
//         doNothing().when(bdService).resetDatabase();

//         // Act & Assert (Ação e Verificação)
//         mockMvc.perform(post("/api/db/restaurarBanco"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("Banco de dados (H2) resetado com sucesso!"));
//     }

//     @Test
//     void quandoRestaurarBanco_eServicoLancaExcecao_entaoRetornaStatus500InternalError() throws Exception {
//         // Arrange (Preparação)
//         // Diz ao mock do serviço para lançar uma exceção quando o método for chamado.
//         String mensagemErro = "Falha de conexão com o banco";
//         doThrow(new RuntimeException(mensagemErro)).when(bdService).resetDatabase();

//         // Act & Assert (Ação e Verificação)
//         mockMvc.perform(post("/api/db/restaurarBanco"))
//                 .andExpect(status().isInternalServerError())
//                 .andExpect(content().string("Falha ao resetar o banco de dados: " + mensagemErro));
//     }
// }
