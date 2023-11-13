package cvb.com.br.petshop.util

import org.mockito.ArgumentCaptor
import org.mockito.Mockito

object MockitoUtil {

    fun <T> anyMockitoInstanceOf(type: Class<T>): T = Mockito.any<T>(type)

    fun <T> captureArgument(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()

}